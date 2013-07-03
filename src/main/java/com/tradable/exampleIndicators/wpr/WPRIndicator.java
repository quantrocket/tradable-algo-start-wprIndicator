package com.tradable.exampleIndicators.wpr;

import com.tradable.api.algo.dataseries.Bar;
import com.tradable.api.algo.indicator.Indicator;
import com.tradable.api.algo.indicator.IndicatorContext;

public class WPRIndicator extends Indicator<Bar> {
	private int period;

	public void setPeriod(int period) {
		this.period = period;
	}

	@Override
	public void recalculate(IndicatorContext<Bar> context) {
		int bars = context.getBufferSize(); 
		if (bars < period) return;
		int start = bars - period - 1;
		int lastProcessedIndex = context.getLastProcessedIndex();
		if (bars - lastProcessedIndex > period) {
			start = lastProcessedIndex;
		}
		double epsilon = 0.0000001;
		for (int i = start; i >= 0; i--) {
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (int j = i; j < i + period; j++) {
				Bar bar = context.getEvent(j);
				if (bar.getLow() < min) {
					min = bar.getLow();
				}
				if (bar.getHigh() > max) {
					max = bar.getHigh();
				}
			}
			Bar bar = context.getEvent(i);
			if (max - min > epsilon) { // prevent division by zero
				context.put(i, 0, -100 * (max - bar.getClose()) / (max - min));
			}
			else {
				context.put(i, 0, 0);
			}
		}
	}

}
