pragma solidity ^0.4.24;

import "./ERC721.sol";
import "./ERC165.sol";
import "./Permissions.sol";

contract DrugSupply is ERC721, ERC165, Permissions {
    struct DrugUnit {
        uint256 serialNumber;
        bytes ipfsHash;
        address owner;
        address approved;
    }
    
    mapping (uint256 => DrugUnit) public drugRegistry;
    mapping (address => uint256) public balances; // Compliance with ERC 721
    mapping (address => mapping(address => bool)) public operators;
    mapping (address => uint256) public permissions;
    
    constructor() public {
        permissions[msg.sender] = ~uint256(0);
    }
    
    /// @dev This event is emitted whenever a drug is transferred from one party to another.
    /// The _tokenId is the serial number of the drug
    /// The ipfsHash is the IPFS hash of supplemental data to be included.
    /// DrugDestroyed(uint256 indexed _tokenId, bytes indexed ipfsHash, timestamp) => DrugTransfer(owner, 0x0, _tokenId, ipfsHash, timestamp)
    /// DrugCreated(uint256 indexed _tokenId, bytes indexed ipfsHash, timestamp) => DrugCreated(0x0, msg.sender, _tokenId, ipfsHash, timestamp)
    event DrugTransfer(address indexed _from, address indexed _to, uint256 indexed _tokenId, bytes ipfsHash, uint timestamp);
    
    // ERC 721 interface
    
    function balanceOf(address _owner) external view returns (uint256) {
        return balances[_owner];
    }
    
    function ownerOf(uint256 _tokenId) external view returns (address) {
        address owner = drugRegistry[_tokenId].owner;
        require(owner != address(0x0), "Invalid token.");
        return owner;
    }
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes data) external payable {
        internalSafeTransferFrom(_from, _to, _tokenId, data);
        emit DrugTransfer(_from, _to, _tokenId, data, now);
    }
    
    function getCodeSize(address _address) internal view returns (uint) {
        uint size;
        assembly { size := extcodesize(_address) }
        return size;
    }
    
    function internalSafeTransferFrom(address _from, address _to, uint256 _tokenId, bytes _data) internal {
        internalTransferFrom(_from, _to, _tokenId);
        if (getCodeSize(_to) > 0) {
            bytes4 expectedReturn = bytes4(keccak256("onERC721Received(address,address,uint256,bytes)"));
            ERC721TokenReceiver receiver = ERC721TokenReceiver(_to);
            require(expectedReturn == receiver.onERC721Received(msg.sender, _from, _tokenId, _data),
                                    "Improper return value");
        }
    }
    
    function safeTransferFrom(address _from, address _to, uint256 _tokenId) external payable {
        internalSafeTransferFrom(_from, _to, _tokenId, "");
    }
    
    function transferFrom(address _from, address _to, uint256 _tokenId) external payable {
        internalTransferFrom(_from, _to, _tokenId);
    }
    
    function internalTransferFrom(address _from, address _to, uint256 _tokenId) internal {
        DrugUnit storage unit = drugRegistry[_tokenId];
        require(_to != address(0x0), "Cannot transfer to 0x0 address.");
        require(unit.owner == _from, "Owner did not own this token");
        require(unit.owner == msg.sender || unit.approved == msg.sender || operators[unit.owner][msg.sender], "Not approved");
        
        unit.owner = _to;
        unit.approved = address(0x0);
        
        emit Transfer(_from, _to, _tokenId);
    }
    
    function approve(address _approved, uint256 _tokenId) external payable {
        DrugUnit storage unit = drugRegistry[_tokenId];
        require(unit.owner == msg.sender || operators[unit.owner][msg.sender], "Not approved or owner");
        unit.approved = _approved;
        
        emit Approval(unit.owner, _approved, _tokenId);
    }
    
    function setApprovalForAll(address _operator, bool _approved) external {
        operators[msg.sender][_operator] = _approved;
        
        emit ApprovalForAll(msg.sender, _operator, _approved);
    }
    
    function getApproved(uint256 _tokenId) external view returns (address) {
        return drugRegistry[_tokenId].approved;
    }
  
    
    function isApprovedForAll(address _owner, address _operator) external view returns (bool) {
        return operators[_owner][_operator];
    }
    
    function supportsInterface(bytes4 interfaceID) external view returns (bool) {
        return interfaceID == 0x150b7a02;
    }
    
    // Users functions
    modifier checkPermission(uint256 perm) {
        require((permissions[msg.sender] & perm) > 0);
        _;
    }
    
    struct UserInformation {
        bytes producerInformationIPFS;
        bytes customInformationIPFS;
        bytes adminInformationIPFS;
    }
    
    mapping (address => UserInformation) public users;
    
    event ProducerAdded(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    event ProducerRemoved(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    event ProducerInformationUpdated(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    
    function addProducer(address addr, bytes ipfsHash) external checkPermission(ADD_PRODUCER) {
        require(ipfsHash.length != 0, "Invalid IPFS hash.");
        users[addr].producerInformationIPFS = ipfsHash;
        emit ProducerAdded(addr, ipfsHash, now);
    }
    
    function updateProducerInformation(address addr, bytes ipfsHash) external checkPermission(MODIFY_PRODUCER) {
        require(ipfsHash.length != 0, "Invalid IPFS hash.");
        users[addr].producerInformationIPFS = ipfsHash;
        emit ProducerInformationUpdated(addr, ipfsHash, now);
    }
    
    function removeProducer(address addr, bytes ipfsHash) external checkPermission(REMOVE_PRODUCER) {
        delete users[addr].producerInformationIPFS;
        emit ProducerRemoved(addr, ipfsHash, now);
    }
    
    function removeProducer(bytes ipfsHash) external {
        delete users[msg.sender].producerInformationIPFS;
        emit ProducerRemoved(msg.sender, ipfsHash, now);
    }
    
    
    event InformationUpdated(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    
    function updateUserInformation(bytes ipfsHash) external {
        users[msg.sender].customInformationIPFS = ipfsHash;
        emit InformationUpdated(msg.sender, ipfsHash, now);
    }
    
    event AdminInformationUpdated(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    
    function updateAdministrativeUserInformation(address addr, bytes ipfsHash) external checkPermission(EDIT_USER_NOTES) {
        users[addr].adminInformationIPFS = ipfsHash;
        emit AdminInformationUpdated(addr, ipfsHash, now);
    }
    
    event AdminUserDeleted(address indexed _address, bytes indexed _ipfshash, uint timestamp);
    
    function deleteUser(address addr, bytes ipfsHash) external checkPermission(DELETE_USER) {
        delete users[addr];
        emit AdminUserDeleted(addr, ipfsHash, now);
    }
    
    // Drug registration and consumption
    uint256 public currentSerial = 0;
    
    function createDrugEntries(bytes ipfsHash, uint256 number) external returns (uint256 startSerial) {
        
        for (uint256 serialNumber = currentSerial; serialNumber < number + currentSerial; serialNumber++) {
            emit DrugTransfer(address(0x0), msg.sender, serialNumber, ipfsHash, now);
            DrugUnit storage unit = drugRegistry[serialNumber];
            unit.serialNumber = serialNumber;
            unit.ipfsHash = ipfsHash;
            unit.owner = msg.sender;
        }
        startSerial = currentSerial;
        currentSerial += number;
    }
    
    function transferForConsumption(address _from, address _to, uint256 _tokenId, bytes ipfsHash) external {
        internalSafeTransferFrom(_from, _to, _tokenId, ipfsHash);
        delete drugRegistry[_tokenId];
        emit DrugTransfer(_to, address(0x0), _tokenId, ipfsHash, now);
    }
    
    function destroyDrugs(uint256 startSerial, uint256 endSerial, bytes ipfsHash) external {
        for (uint256 _tokenId = startSerial; _tokenId < endSerial; _tokenId++) {
            if (drugRegistry[_tokenId].owner == msg.sender) {
                delete drugRegistry[_tokenId];
                emit DrugTransfer(msg.sender, address(0x0), _tokenId, ipfsHash, now);
            }
        }
    }
    
    // contract
    function destroy() external checkPermission(DESTROY_CONTRACT) {
        selfdestruct(msg.sender);
    }
    
    function withdraw(uint256 amount) external checkPermission(WITHDRAW_FUNDS) {
        msg.sender.transfer(amount);
    }

}