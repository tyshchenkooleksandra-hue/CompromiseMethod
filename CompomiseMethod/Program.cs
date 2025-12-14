class Program
{
    static void Main()
    {
        var decisionMatrix = new double[,]
        {
            { 3, 8, 7, 9 },
            { 5, 6, 3, 8 },
            { 4, 9, 9, 4 },
            { 6, 4, 5, 4 }
        };

        var criteriaWeights = new[] { 0.25, 0.15, 0.40, 0.20 };

        var waldWeight = 1.0 / 3.0;
        var riskWeight = 2.0 / 3.0;

        var waldNormalized = CalculateNormalizedWald(decisionMatrix);
        var riskNormalized = CalculateNormalizedRisk(
            decisionMatrix,
            criteriaWeights
        );

        var overallScores = CombineCriteria(
            waldNormalized,
            riskNormalized,
            waldWeight,
            riskWeight
        );

        var bestAlternativeIndex = GetBestAlternativeIndex(overallScores);

        PrintResults(
            waldNormalized,
            riskNormalized,
            overallScores,
            bestAlternativeIndex
        );
    }


    static double[] CalculateNormalizedWald(double[,] matrix)
    {
        var alternatives = matrix.GetLength(0);
        var criteria = matrix.GetLength(1);

        var waldValues = new double[alternatives];

        for (var i = 0; i < alternatives; i++)
        {
            waldValues[i] = Enumerable.Range(0, criteria)
                                      .Min(j => matrix[i, j]);
        }

        return NormalizePositiveCriterion(waldValues);
    }


    static double[] CalculateNormalizedRisk(
        double[,] matrix,
        double[] weights)
    {
        var alternatives = matrix.GetLength(0);
        var criteria = matrix.GetLength(1);

        var riskStdDev = new double[alternatives];

        for (var i = 0; i < alternatives; i++)
        {
            var weightedMean = 0.0;
            for (var j = 0; j < criteria; j++)
                weightedMean += weights[j] * matrix[i, j];

            var variance = 0.0;
            for (var j = 0; j < criteria; j++)
                variance += weights[j] *
                            Math.Pow(matrix[i, j] - weightedMean, 2);

            riskStdDev[i] = Math.Sqrt(variance);
        }

        return NormalizeNegativeCriterion(riskStdDev);
    }


    static double[] CombineCriteria(
        double[] wald,
        double[] risk,
        double waldWeight,
        double riskWeight)
    {
        var n = wald.Length;
        var scores = new double[n];

        for (var i = 0; i < n; i++)
        {
            scores[i] =
                waldWeight * wald[i] +
                riskWeight * risk[i];
        }

        return scores;
    }

    static int GetBestAlternativeIndex(double[] scores)
    {
        return Array.IndexOf(scores, scores.Max());
    }


    static double[] NormalizePositiveCriterion(double[] values)
    {
        var min = values.Min();
        var max = values.Max();

        return values
            .Select(v => (v - min) / (max - min))
            .ToArray();
    }

    static double[] NormalizeNegativeCriterion(double[] values)
    {
        var min = values.Min();
        var max = values.Max();

        return values
            .Select(v => (max - v) / (max - min))
            .ToArray();
    }


    static void PrintResults(
        double[] waldNorm,
        double[] riskNorm,
        double[] overallScores,
        int bestIndex)
    {
        Console.WriteLine("Alternative  | Wald |  Risk    | Final score");

        for (var i = 0; i < overallScores.Length; i++)
        {
            Console.WriteLine(
                $"A{i + 1,11} | {waldNorm[i],4:F2} | {riskNorm[i],8:F2} | {overallScores[i],11:F3}"
            );
        }

        Console.WriteLine($"\nOptimal alternative: A{bestIndex + 1}");
    }
}
