package tr.com.muskar.crypto_analysis_bot.trader;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import tr.com.muskar.crypto_analysis_bot.model.FeatureVector;

public class MLModelEvaluator {
    private final RandomForest model;

    public MLModelEvaluator(RandomForest model) {
        this.model = model;
    }

    public int predict(FeatureVector fv) {
        DataFrame input = DataFrame.of(
                DoubleVector.of("rsi", new double[]{fv.getRsi()}),
                DoubleVector.of("macd", new double[]{fv.getMacd()}),
                DoubleVector.of("bollinger_score", new double[]{fv.getBollingerPercentB()}),
                DoubleVector.of("price_change", new double[]{fv.getPriceChangePercent()})
        );
        return model.predict(input)[0];
    }
}

