import './stylesheet.css';
import MetricChart from './MetricChart';

function CommandComponent(prop) {
    // console.log(prop.data)
    let errorPercentFloat = (1 - (prop.data.successCount / prop.data.total)) * 100;
    const errorPercent = round(errorPercentFloat, 1);
    // console.log(errorPercentFloat,errorPercent)
    return (
        <div class="container">
            <div class="topRow">
                <MetricChart data={prop.data} />
                <div class="cmdMetrics">
                    <div class="commandName">{prop.data.commandName}</div>
                    <div class="metrics-data">
                        <div class="col1 column">
                            <div class="successCount metricValue">{prop.data.successCount}</div>
                            <div class="circuitCount metricValue">0</div>
                        </div>
                        <div class="col2 column">
                            <div class="timeoutCount metricValue">
                                {prop.data.timeoutCount}
                            </div>
                            <div class="threadPoolRejectedCount metricValue">
                                {prop.data.threadPoolRejectedCount}
                            </div>
                            <div class="errorCount metricValue">
                                {prop.data.errorCount}
                            </div>
                        </div>
                        <div class="col3 column">
                            <div class="errorPercentage metricValue">
                                {errorPercent}%
                            </div>
                        </div>
                    </div>
                    <div class="requestRate">
                        <div class="host">
                            <div class="label">Host : </div>
                            <div class="value">54.0/2</div>
                        </div>
                        <div class="cluster">
                            <div class="label">Cluster : </div>
                            <div class="value">20,056.0/s</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="bottomRow">
                <div class="circuitStatus">
                    <div class="label">Circuit : </div>
                    <div class="cvalue">{prop.data.circuitStatus}</div>
                </div>
                <div class="latencyMetrics">
                    <div class="lcol1">
                        <div class="llabel">Hosts</div>
                        <div class="llabel">Median</div>
                        <div class="llabel">Mean</div>
                    </div>
                    <div class="lcol2">
                        <div class="lvalue">1</div>
                        <div class="lvalue">1ms</div>
                        <div class="lvalue">4ms</div>
                    </div>
                    <div class="lcol3">
                        <div class="llabel">90th</div>
                        <div class="llabel">99th</div>
                        <div class="llabel">99.5th</div>
                    </div>
                    <div class="lcol4">
                        <div class="lvalue"> {prop.data.latency.p50}ms</div>
                        <div class="lvalue">{prop.data.latency.p90}ms</div>
                        <div class="lvalue">{Math.floor(prop.data.latency.p99)}ms</div>
                    </div>
                </div>
            </div>
        </div>
    );
}

function round(value, precision) {
    var multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
}
export default CommandComponent;