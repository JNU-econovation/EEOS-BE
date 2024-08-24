package com.blackcompany.eeos.common;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Component
public class DataClearExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		DatabaseCleaner dataCleaner = getDataCleaner(context);
		dataCleaner.clear();
	}

	private DatabaseCleaner getDataCleaner(final ExtensionContext extensionContext) {
		return SpringExtension.getApplicationContext(extensionContext).getBean(DatabaseCleaner.class);
	}
}
