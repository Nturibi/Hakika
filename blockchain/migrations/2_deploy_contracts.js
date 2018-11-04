var DrugSupply = artifacts.require("DrugSupply");

module.exports = function(deployer) {
  deployer.deploy(DrugSupply);
};
