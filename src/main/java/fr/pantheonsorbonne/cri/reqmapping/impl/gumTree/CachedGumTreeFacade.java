package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import fr.pantheonsorbonne.cri.reqmapping.LineReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import jetbrains.exodus.core.crypto.MessageDigestUtil;
import jetbrains.exodus.entitystore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CachedGumTreeFacade extends GumTreeFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger("CachedGumTreeFacade");
    private final static PersistentEntityStore entityStore = PersistentEntityStores.newInstance(Path.of(System.getProperty("user.home"),".dextormCache").toString());


    @Override
    public List<ReqMatch> getReqMatcher(String filePath, List<Diff> diffs, boolean doMethods, boolean doInstructions) {
        var idRaw = filePath + "-" + diffs.stream().map(d -> d.commitId).collect(Collectors.joining("-")) + "-" + doMethods + "-" + doInstructions;
        String id = idRaw;

        {

            StoreTransaction txnRO = entityStore.beginReadonlyTransaction();

            final EntityIterable candidates = txnRO.find("ReqMatchers", "reqMatcherId", id);

            if (!candidates.isEmpty()) {
                Type type = new TypeToken<ArrayList<LineReqMatchImpl>>() {
                }.getType();
                List<ReqMatch> outList = new Gson().fromJson(candidates.getFirst().getBlobString("content"), type);
                txnRO.abort();
                return outList;


            } else {
                txnRO.abort();
                var res = super.getReqMatcher(filePath, diffs, doMethods, doInstructions);
                entityStore.executeInTransaction(txn -> {


                    final Entity reqMatchers = txn.newEntity("ReqMatchers");

                    reqMatchers.setProperty("reqMatcherId", id);

                    String jsonReqMatch = new Gson().toJson(res);
                    reqMatchers.setBlobString("content", jsonReqMatch);
                });
                return res;
            }
        }


    }
}
