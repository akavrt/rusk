package com.akavrt.rusk.ssscsp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:59
 */
public class Utils {
    private static final Logger LOGGER = LogManager.getLogger(Utils.class);

    public static boolean isPatternFeasible(int[] cuts, Problem problem) {
        if (cuts == null) {
            LOGGER.warn("Incompatible pattern encountered: cuts array is null");
            return false;
        }

        if (cuts.length != problem.size()) {
            LOGGER.warn("Incompatible pattern encountered: cuts.length is {}, problem.size() is {}",
                        cuts.length, problem.size());
            return false;
        }

        int width = 0;
        for (int i = 0; i < problem.size(); i++) {
            width += cuts[i] * problem.getOrder(i).getWidth();
        }

        if (width == 0) {
            LOGGER.debug("Inactive pattern encountered.");
            return false;
        }

        if (width > problem.getStockWidth()) {
            LOGGER.warn("Infeasible pattern encountered with width {} exceeds stock width of {}.",
                        width, problem.getStockWidth());
            return false;
        }

        return true;
    }

}
