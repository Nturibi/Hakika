var HDWalletProvider = require("truffle-hdwallet-provider");
var mnemonic = "mention purpose motor ride parent distance zoo gentle family chief marriage remain";
var mnemonicGanache = "gospel enact jaguar burger curtain owner nuclear service civil tenant helmet swap";
module.exports = {
  networks: {
      ropsten: {
        provider: function() {
          return new HDWalletProvider(mnemonic, "https://ropsten.infura.io/");
        },
        network_id: '3',
      },
      test: {
        provider: function() {
          return new HDWalletProvider(mnemonicGanache, "http://127.0.0.1:8545/");
        },
        network_id: '*',
      },
  }
};