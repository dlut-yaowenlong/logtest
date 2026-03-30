package customer.log_test.handler;

import com.sap.cds.ql.Select;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.catalogservice.CatalogService_;


@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CategoryHandler implements EventHandler {

    @Autowired
    PersistenceService db;

    @On(event = "READ", entity = "CatalogService.Categories")
    public void onReadCategories(CdsReadEventContext context) {
        System.out.println("Categories READ called");
        System.out.println("CQN = " + context.getCqn());

        var result = db.run(context.getCqn());
        context.setResult(result);
    }
}