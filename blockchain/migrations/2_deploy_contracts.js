var DrugSupply = artifacts.require("./DrugSupply.sol");

module.exports = function(deployer) {
  deployer.deploy(DrugSupply);
};
