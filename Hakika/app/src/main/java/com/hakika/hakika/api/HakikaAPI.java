package com.hakika.hakika.api;

import android.util.Log;

import com.hakika.hakika.R;
import com.hakika.hakika.api.models.Drug;
import com.hakika.hakika.api.models.DrugTransfer;
import com.hakika.hakika.api.models.IPFSTag;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.ipfs.api.IPFS;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;
import retrofit2.Retrofit;
import rx.Observable;

public class HakikaAPI {
    private Web3j web3j;
    private IPFS ipfs;
    private DrugSupply contract;
    private Credentials credentials;

    private static final BigInteger GAS_PRICE = new BigInteger("1000000000");
    private static final BigInteger GAS_LIMIT = new BigInteger("500000");

    private static HakikaAPI adminAPI;
    public static HakikaAPI createAdminAPI() {
        if (adminAPI != null) return adminAPI;
        return adminAPI = new HakikaAPI("https://sokol.poa.network:443/", "gateway.ipfs.io",
                "0xd77633309a48eb435498972bda287e208c9c10a2",
                "mention purpose motor ride parent distance zoo gentle family chief marriage remain");
    }

    private static HakikaAPI restrictedAPI;
    public static HakikaAPI createRestrictedAPI() {
        if (restrictedAPI != null) return restrictedAPI;
        return restrictedAPI = new HakikaAPI("https://sokol.poa.network:443/", "gateway.ipfs.io",
                "0xd77633309a48eb435498972bda287e208c9c10a2", null);
    }

    // IPFS endpoint: https://gateway.ipfs.io
    // web3 endpoint: https://sokol.poa.network:443/
    public HakikaAPI(String web3Endpoint, String ipfsEndpoint, String contractAddress,
                     String mnemonic) {
        web3j = Web3jFactory.build(new HttpService(web3Endpoint));
        ipfs = new IPFS(ipfsEndpoint, 443, "/api/v0/", true);
        contract = DrugSupply.load(contractAddress, web3j,
                credentials, GAS_PRICE, GAS_LIMIT);
        if (mnemonic != null) {
            credentials = WalletUtils.loadBip39Credentials(null, mnemonic);
        } else {
            credentials = WalletUtils.loadBip39Credentials(null,
                    "gospel enact jaguar burger curtain owner nuclear service civil tenant helmet swap");
        }
    }

    public static byte[] getIPFSFile(IPFS ipfs, byte[] ipfsKey) throws IOException {
        return ipfs.cat(new Multihash(ipfsKey));
    }

    public static JSONObject getIPFSJson(IPFS ipfs, byte[] ipfsKey) throws IOException {
        try {
            return new JSONObject(new String(getIPFSFile(ipfs, ipfsKey), StandardCharsets.UTF_8));
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public static Multihash uploadToIPFS(IPFS ipfs, byte[] data) throws IOException {
        return ipfs.add(new NamedStreamable.ByteArrayWrapper(data)).get(0).hash;
    }

    public List<DrugTransfer> getTransferHistory(String serialNumber) {

        Observable<DrugSupply.DrugTransferEventResponse> observable =
                contract.drugTransferEventObservable(DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST);
        List<DrugTransfer> transfers = new ArrayList<>();
        observable.map(dter -> {
           return new DrugTransfer(dter._from, dter._to, dter._tokenId.toString(), dter.ipfsHash,
                   new Date(dter.timestamp.longValue()));
        }).forEach(dt -> {
            transfers.add(dt);
        });
        return transfers;
    }

    public List<String> getOwnedDrugs(String person) {
        Observable<DrugSupply.DrugTransferEventResponse> observable =
                contract.drugTransferEventObservable(DefaultBlockParameterName.EARLIEST,
                        DefaultBlockParameterName.LATEST);
        Set<String> ownedDrugs = new HashSet<>();
        observable.forEach(dter -> {
            if (dter._to.equalsIgnoreCase(person)) {
                ownedDrugs.add(dter._tokenId+"");
            }

            if (dter._from.equalsIgnoreCase(person)) {
                ownedDrugs.remove(dter._tokenId+"");
            }
        });
        return new ArrayList<>(ownedDrugs);
    }

    public IPFSTag getUserInformation(String user) throws IOException {
        try {
            IPFSTag ipfsTag = new IPFSTag(contract.users(user).send().getValue2());
            ipfsTag.fetchFromIPFS(ipfs);
            return ipfsTag;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    public IPFSTag getProducerInformation(String producer) throws IOException {
        try {
            IPFSTag ipfsTag = new IPFSTag(contract.users(producer).send().getValue1());
            ipfsTag.fetchFromIPFS(ipfs);
            return ipfsTag;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    public String getFriendlyUserName(String user) throws IOException {
        return getUserInformation(user).getExtraAttributes().get("friendlyName");
    }

    public String getFriendlyProducerName(String producer) throws IOException {
        return getProducerInformation(producer).getExtraAttributes().get("friendlyName");
    }

    public Drug getDrugInformation(String serialNumber) throws IOException {
        try {
            Tuple4<BigInteger, byte[], String, String> tup = contract.drugRegistry(new BigInteger(serialNumber)).send();
            String currentOwner = tup.getValue3();
            List<DrugTransfer> history = getTransferHistory(serialNumber);
            byte[] ipfsHash = tup.getValue2();

            if (!serialNumber.equalsIgnoreCase(tup.getValue1().toString())) {
                throw new RuntimeException("The serial number "+serialNumber+" did not match the tuple: "+tup);
            }

            Drug drug = new Drug(serialNumber, history, currentOwner, ipfsHash);
            drug.getTag().fetchFromIPFS(ipfs);
            return drug;
        } catch (Exception exc) {
            throw new IOException(exc);
        }

    }

    public void setUserInformation(Map<String, byte[]> attachments,
                                   Map<String, String> extraAttributes) throws IOException {
        IPFSTag tag = IPFSTag.uploadTag(ipfs, attachments, extraAttributes);
        if (tag == null) {
            Log.e("HakikaAPI", "setUserInformation could not get tag.");
            return;
        }
        contract.updateUserInformation(tag.getIpfsKey());
    }

    public void setProducerInformation(String producer, Map<String, byte[]> attachments,
                                       Map<String, String> extraAttributes) throws IOException {
        IPFSTag tag = IPFSTag.uploadTag(ipfs, attachments, extraAttributes);
        if (tag == null) {
            Log.e("HakikaAPI", "setProducerInformation could not get tag.");
            return;
        }
        contract.updateProducerInformation(producer, tag.getIpfsKey());
    }

    public List<String> addDrugs(String genericName, String name, String productionLocation,
                           Date expirationDate, Date productionDate, long batchNumber,
                           String useInformation, Map<String, String> extraAttributes,
                                 Map<String, byte[]> attachments, long number) throws IOException {
        String exprDate = expirationDate.getTime()+"";
        String prodDate = productionDate.getTime()+"";
        String batchString = batchNumber+"";

        extraAttributes.put("genericName", genericName);
        extraAttributes.put("name", name);
        extraAttributes.put("productionDate", prodDate);
        extraAttributes.put("expirationDate", exprDate);
        extraAttributes.put("batchNumber", batchString);
        extraAttributes.put("useInformation", useInformation);
        extraAttributes.put("productionLocation", productionLocation);
        IPFSTag tag = IPFSTag.uploadTag(ipfs, attachments, extraAttributes);

        if (tag == null) return new ArrayList<>();

        try {
            TransactionReceipt rcpt = contract.createDrugEntries(tag.getIpfsKey(),
                    new BigInteger(number + "")).send();
            List<DrugSupply.DrugTransferEventResponse> dters = contract.getDrugTransferEvents(rcpt);
            List<String> serials = new ArrayList<>();
            for (DrugSupply.DrugTransferEventResponse dter : dters) {
                serials.add(dter._tokenId+"");
            }
            return serials;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    public boolean isProducer(String user) {
        try {
            return contract.isVerifiedProducer(user).send();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }

    public void transferDrug(String to, String serialNumber, IPFSTag tag) {
        contract.safeTransferFrom(credentials.getAddress(), to,
                new BigInteger(serialNumber), tag.getIpfsKey(), BigInteger.ZERO);
    }

    public void transferDrugAndDestroy(String to, String serialNumber, IPFSTag tag) {
        contract.transferForConsumption(credentials.getAddress(), to,
                new BigInteger(serialNumber), tag.getIpfsKey());
    }

    public void destroyDrugs(String startSerial, String endSerial, IPFSTag tag) {
        contract.destroyDrugs(new BigInteger(startSerial), new BigInteger(endSerial), tag.getIpfsKey());
    }

    public void addProducer(String producer, IPFSTag ipfsTag) {
        contract.addProducer(producer, ipfsTag.getIpfsKey());
    }

}
