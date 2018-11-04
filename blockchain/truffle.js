var HDWalletProvider = require("truffle-hdwallet-provider");
var mnemonic = "mention purpose motor ride parent distance zoo gentle family chief marriage remain";
var mnemonicGanache = "gospel enact jaguar burger curtain owner nuclear service civil tenant helmet swap";
module.exports = {
  networks: {
      sokol: {
        provider: function() {
          return new HDWalletProvider(mnemonic, "https://sokol.poa.network:443/");
        },
        network_id: '77',
      },
      development: {
        provider: function() {
          return new HDWalletProvider(mnemonicGanache, "http://127.0.0.1:7545/");
        },
        network_id: '*',
      },
  }
};
