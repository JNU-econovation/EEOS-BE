package com.blackcompany.eeos.common;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Component
public class DataClearExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		ApplicationContext appContext = SpringExtension.getApplicationContext(context);
		List<DatabaseCleaner> dataCleaners = getDataCleaners(appContext);

		for (DatabaseCleaner cleaner : dataCleaners) {
			cleaner.clear();
		}
	}

	private List<DatabaseCleaner> getDataCleaners(ApplicationContext appContext) {
		return new ArrayList<>(appContext.getBeansOfType(DatabaseCleaner.class).values());
	}
}
