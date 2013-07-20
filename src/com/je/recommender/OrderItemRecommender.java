package com.je.recommender;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
//import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class OrderItemRecommender implements Recommender {

	private final Recommender delegate;
	private final int neighborhoodSize = 3;

	public OrderItemRecommender() throws TasteException, IOException {

		this(new GenericBooleanPrefDataModel(
				GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
						getSourceFile()))));
	}

	public OrderItemRecommender(DataModel model) throws TasteException,
			IOException {

		// UserSimilarity similarity = new LogLikelihoodSimilarity(model);
		UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(
				neighborhoodSize, similarity, model);

		delegate = new GenericBooleanPrefUserBasedRecommender(model,
				neighborhood, similarity);
	}

	public List<RecommendedItem> recommend(long entityId, int howMany)
			throws TasteException {

		return delegate.recommend(entityId, howMany, null);
	}

	public List<RecommendedItem> recommend(long entityId, int howMany,
			IDRescorer rescorer) throws TasteException {

		return delegate.recommend(entityId, howMany, rescorer);
	}

	public float estimatePreference(long entityId, long itemID)
			throws TasteException {

		return delegate.estimatePreference(entityId, itemID);
	}

	public void setPreference(long entityId, long itemID, float value)
			throws TasteException {

		delegate.setPreference(entityId, itemID, value);
	}

	public void removePreference(long entityId, long itemID)
			throws TasteException {

		delegate.removePreference(entityId, itemID);
	}

	public DataModel getDataModel() {

		return delegate.getDataModel();
	}

	public void refresh(Collection<Refreshable> alreadyRefreshed) {

		delegate.refresh(alreadyRefreshed);
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
