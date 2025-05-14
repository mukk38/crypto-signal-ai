package tr.com.muskar.crypto_analysis_bot.trader;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;

import java.nio.file.Paths;

public class MLTrainer {
    public RandomForest trainModel(String csvPath) throws Exception {
        DataFrame df = Read.csv(Paths.get(csvPath));

        Formula formula = Formula.lhs("label");

        RandomForest model = RandomForest.fit(formula, df);
        int[] predictions = model.predict(df);
        int[] actual = df.intVector("label").toIntArray();
        int correct = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == predictions[i]) correct++;
        }
        System.out.println("Accuracy: " + (correct * 100.0 / actual.length) + "%");
        return model;
    }
}
