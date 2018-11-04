pragma solidity ^0.4.24;

contract Permissions {
    uint256 constant SET_PERMISSIONS = 1 << 0;
    uint256 constant ADD_PRODUCER = 1 << 1;
    uint256 constant REMOVE_PRODUCER = 1 << 2;
    uint256 constant MODIFY_PRODUCER = 1 << 3;
    uint256 constant EDIT_USER_NOTES = 1 << 4;
    uint256 constant DELETE_USER = 1 << 5;
    uint256 constant DESTROY_CONTRACT = 1 << 6;
    uint256 constant WITHDRAW_FUNDS = 1 << 7;
}