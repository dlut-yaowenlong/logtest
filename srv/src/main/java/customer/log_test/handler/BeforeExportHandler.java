package customer.log_test.handler;

import cds.gen.catalogservice.CatalogService_;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class BeforeExportHandler  implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(BeforeExportHandler.class);

    @On(event = "beforeExport", service = CatalogService_.CDS_NAME)
    public String beforeExport(EventContext context) {
        logger.info("BeforeExportHandler: beforeExport event triggered");
        // You can add your custom logic here
        return "Before export logic executed successfully.";
    }
    
}
