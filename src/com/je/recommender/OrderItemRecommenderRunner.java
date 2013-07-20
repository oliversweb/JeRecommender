package com.je.recommender;

import java.util.List;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

final class OrderItemRecommenderRunner {

	OrderItemRecommenderRunner() { }

	public static void main(String[] args) throws Exception {

		int howMany = 5;

		PreferenceArray prefs = getFakeAnonymousBooleanPreferences(args);
		
		OrderItemWithAnonymousRecommender recommender = new OrderItemWithAnonymousRecommender();

		List<RecommendedItem> recommendations = recommender.recommend(prefs,
				howMany);

		if (recommendations.size() == 0) {
			System.out.print("RecommendedItem[]");
		}

		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}
	}

	private static PreferenceArray getFakeAnonymousBooleanPreferences(String[] itemIds) {

		PreferenceArray anonymousPrefs = new GenericUserPreferenceArray(itemIds.length);

		int counter = 0;
		for (String i : itemIds) {
			anonymousPrefs.setItemID(counter++, Long.valueOf(i));
		}

		return (anonymousPrefs);
	}
}
