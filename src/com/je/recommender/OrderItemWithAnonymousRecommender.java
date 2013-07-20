package com.je.recommender;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousUserDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class OrderItemWithAnonymousRecommender extends OrderItemRecommender {

	private static PlusAnonymousUserDataModel plusAnonymousModel = null;

	public OrderItemWithAnonymousRecommender() throws TasteException, IOException {	
	
		this(new GenericBooleanPrefDataModel(
				GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
						getSourceFile()))));
	}

	public OrderItemWithAnonymousRecommender(DataModel model)
			throws TasteException, IOException {
	
		super(new PlusAnonymousUserDataModel(model));

		plusAnonymousModel = (PlusAnonymousUserDataModel) getDataModel();
	}

	public synchronized List<RecommendedItem> recommend(PreferenceArray anonymousUserPrefs, int howMany)
			throws TasteException {

		plusAnonymousModel.setTempPrefs(anonymousUserPrefs);

		List<RecommendedItem> recommendations = recommend(
				PlusAnonymousUserDataModel.TEMP_USER_ID, howMany, null);

		plusAnonymousModel.clearTempPrefs();

		return recommendations;
	}
	
	private static File getSourceFile() throws IOException {
		
		String source_file_name = "order_data.dat";
		
		File file = new File(source_file_name);
				
		if (!file.exists()) {					
			
			String message = String.format("The source file '%s' is missing at the location '%s'", source_file_name, System.getProperty("user.dir"));
			
			throw new IOException(message);
		}
		
		return (file);
	}
}