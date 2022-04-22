package fr.pantheonsorbonne.cri.publisher.db;

import com.google.inject.Singleton;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.AbstractRequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.db.model.CoverageRunContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class DBRequirementPublisher extends AbstractRequirementPublisher {

    final CoverageRunContext context;

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public DBRequirementPublisher() {

        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();

        context = new CoverageRunContext();
        context.setTimestamp(System.currentTimeMillis());
        em.getTransaction().begin();
        em.persist(context);
        em.getTransaction().commit();
    }

    @Override
    protected void publishLinkedRequirement(Requirement req) {


        em.getTransaction().begin();
        fr.pantheonsorbonne.cri.publisher.db.model.Requirement dbReq = new fr.pantheonsorbonne.cri.publisher.db.model.Requirement();
        dbReq.setReqId(Long.valueOf(req.getId()));
        em.getTransaction().commit();

    }
}
