package com.tradable.exampleIndicators.wpr;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.tradable.api.algo.descriptor.parameter.IntegerParameterDescriptor;
import com.tradable.api.algo.descriptor.parameter.ParameterDescriptor;
import com.tradable.api.algo.indicator.DefaultIndicatorPackage;
import com.tradable.api.algo.indicator.Indicator;
import com.tradable.api.algo.indicator.IndicatorDescriptor;
import com.tradable.api.algo.indicator.IndicatorFactory;
import com.tradable.api.algo.indicator.IndicatorInitializingContext;
import com.tradable.api.algo.indicator.IndicatorPackage;
import com.tradable.api.algo.indicator.IndicatorPlot;
import com.tradable.api.algo.indicator.IndicatorSubscriptionKey;
import com.tradable.api.algo.indicator.data.NumericPlotValue;
import com.tradable.api.algo.indicator.painting.ColorPlotPainterDescriptor;
import com.tradable.api.algo.indicator.painting.DefaultPlotPaintingStrategy;
import com.tradable.api.algo.indicator.painting.IndicatorLocation;
import com.tradable.api.algo.indicator.painting.IndicatorPainting;
import com.tradable.api.algo.indicator.painting.IndicatorPaintingBuilder;
import com.tradable.api.algo.indicator.painting.PlotPaintingStrategy;

public class WPRIndicatorFactory extends IndicatorFactory {

	@Override
	public IndicatorPackage getIndicatorPackage() {
		return getPackage();
	}

	@Override
	public Indicator<?> createIndicator(IndicatorSubscriptionKey indicatorSubscriptionKey, IndicatorInitializingContext indicatorInitializingContext) {
		int period = (Integer) indicatorSubscriptionKey.getParams().get("period");
		WPRIndicator indicator = new WPRIndicator();
		indicator.setPeriod(period);
		return indicator;
	}

	public IndicatorPackage getPackage() {
		IndicatorPlot wprPlot = new IndicatorPlot("wprPlot", "%R", "Williams' Percent Range", NumericPlotValue.class);

		List<IndicatorPlot> plots = new ArrayList<IndicatorPlot>();
		plots.add(wprPlot);

		IntegerParameterDescriptor period = new IntegerParameterDescriptor("period", "Period", 14, 1, 10000, 1);
		List<ParameterDescriptor> params = new ArrayList<ParameterDescriptor>();
		params.add(period);
		
		IndicatorPaintingBuilder ipBuilder = new IndicatorPaintingBuilder();
		ipBuilder.setPreferredLocation(IndicatorLocation.SUBGRAPH); // the indicator should be painted on a separate graph
        PlotPaintingStrategy plotPaintingStrategy = DefaultPlotPaintingStrategy.createLinearPainting(1, new ColorPlotPainterDescriptor(Color.CYAN)); // line width 1, color cyan
        ipBuilder.setPlotPaintingStrategy(wprPlot.getPlotId(), plotPaintingStrategy);
        IndicatorPainting painting = ipBuilder.build();

		IndicatorDescriptor descriptor = new IndicatorDescriptor("com.tradable.examples.wpr",
				"WPR",
				"Williams' Percent Range",
				"Determines the overbought and oversold levels of the market.",
				plots,
				params,
				painting);

		List<IndicatorDescriptor> descriptors = new ArrayList<IndicatorDescriptor>();
		descriptors.add(descriptor);

		DefaultIndicatorPackage ret = new DefaultIndicatorPackage("com.tradable.examples.wpr",
				"WPR indicator package",
				descriptors);
		return ret;
	}

}
