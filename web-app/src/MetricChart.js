import React, { Component } from 'react';
import * as d3 from 'd3';

class MetricChart extends Component {
    constructor(props) {
        super(props);
        this.chartRef = React.createRef();
        this.state = { data: props.data }
        // console.log(props.data)
    }
    componentDidMount() {

        let refToChart = d3.select(this.chartRef.current);
        const color = chartColorBasedOnCommandHelath(this.state.data.commandHealth)
        const radius = radiusOfChartBasedOnVolume(this.state.data.total);
        const dataFrame = generateDataForSparkLine(this.state.data.qps);
        console.log(this.state.data.commandName, dataFrame);
        const yMinValue = d3.min(dataFrame, d => d.value);
        const yMaxValue = d3.max(dataFrame, d => d.value);
        const xMinValue = d3.min(dataFrame, d => d.label);
        const xMaxValue = d3.max(dataFrame, d => d.label);

        const xScale = d3
            .scaleLinear()
            .domain([xMinValue, xMaxValue])
            .range([0, 100]);
        const yScale = d3
            .scaleLinear()
            .range([50, 0])
            .domain([0, yMaxValue]);
        const line = d3
            .line()
            .x(d => xScale(d.label))
            .y(d => yScale(d.value))
            .curve(d3.curveMonotoneX);


        refToChart.append("svg")
            .attr("width", 150)
            .attr("height", 150)
            .append("circle")
            .transition()
            .duration(300)
            .attr("cy", "50%")
            .attr("cx", "50%")
            .attr("r", radius)
            .style("fill", color);
        refToChart
            .select("svg")
            .attr("width", 150)
            .attr("height", 150)
            .append('path')
            .datum(dataFrame)
            .attr('fill', 'none')
            .attr('stroke', '#739fc7')
            .attr('stroke-width', 1)
            .attr('d', line)
            .attr('transform', 'translate(30,50)');


    }
    render() {
        return (<g class="ggg" ref={this.chartRef}></g>);
    }


}
const chartColorBasedOnCommandHelath = (commandHealth) => {
    if (commandHealth == "RED") {
        return "#fcbdbb";
    } else if (commandHealth == "YELLOW") {
        return "#fcdfac";
    } else {
        return "#ccdbcb";
    }
}

const radiusOfChartBasedOnVolume = (volume) => {
    if (volume < 10) {
        return 30;
    } else if (volume < 50) {
        return 40;
    } else if (volume < 100) {
        return 50;
    } else if (volume < 500) {
        return 60;
    } else if (volume < 1000) {
        return 70;
    } else {
        return 80;
    }
}

const generateDataForSparkLine = (qps) => {
    const chartData = [];
    for (let i = 0; i < qps.length; i++) {
        const value = qps[i];
        chartData.push({
            label: i,
            value,
            tooltipContent: `<b>x: </b>${i}<br><b>y: </b>${value}`
        });
    }
    return chartData
}

export default MetricChart;