package com.hakika.hakika.api;

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


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return null;
    }

    public IPFSTag getUserInformation(String user) {
        return null;
    }

    public IPFSTag getProducerInformation(String producer) {
        return null;
    }

    public String getFriendlyUserName(String user) {
        return getUserInformation(user).getExtraAttributes().get("friendlyName");
    }

    public String getFriendlyProducerName(String producer) {
        return getProducerInformation(producer).getExtraAttributes().get("friendlyName");
    }

    public Drug fetchDrugInformation(String serialNumber) {
        return null;
    }

    public void setUserInformation(Map<String, byte[]> attachments,
                                   Map<String, String> extraAttributes) {

    }

    public void setProducerInformation(Map<String, byte[]> attachments,
                                       Map<String, String> extraAttributes) throws IOException {
        IPFSTag tag = IPFSTag.uploadTag(ipfs, attachments, extraAttributes);

    }

    public List<String> addDrugs(String genericName, String name, String productionLocation,
                           Date expirationDate, Date productionDate, long batchNumber,
                           String useInformation, Map<String, byte[]> attachments, long number) throws IOException {
        String exprDate = expirationDate.getTime()+"";
        String prodDate = productionDate.getTime()+"";
        String batchString = batchNumber+"";

        Map<String, String> extraAttributes = new HashMap<>();
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



}
