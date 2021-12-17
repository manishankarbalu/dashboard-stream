const commandMessageListner = eventSourceMessageListener = e => {
    console.log(e.data)
};
var proxyStream = "http://127.0.0.1:8080/dashboard.stream";
var source = new EventSource(proxyStream);
source.addEventListener('message', commandMessageListner, false);
source.addEventListener('error', e => {
    if (e.eventPhase == EventSource.CLOSED) {
        console.log("Connection was closed on error: " + e);
        source.close()
    } else {
        console.log("Error occurred while streaming: " + e);
        source.close()
    }
}, false);

var testData = '{"commandMetricsData":[{"qps":[105,131,126,190,131,143,134,153,124,109],"commandHealth":"YELLOW","commandName":"AntiEntropyCommand","circuitStatus":"CLOSED","timeoutCount":1,"successCount":82,"errorCount":81,"threadPoolRejectedCount":0,"latency":{"p90":183.0,"p99":203.07999999999996,"p50":103.0},"total":163},{"qps":[36,41,39,46,50,50,47,43,40,38],"commandHealth":"RED","commandName":"FailureCommand","circuitStatus":"CLOSED","timeoutCount":1,"successCount":0,"errorCount":256,"threadPoolRejectedCount":0,"latency":{"p90":49.0,"p99":55.0,"p50":31.0},"total":256},{"qps":[0,0,0,0,0,0,0,0,0,0],"commandHealth":"RED","commandName":"RejectedCommand","circuitStatus":"CLOSED","timeoutCount":1,"successCount":0,"errorCount":256,"threadPoolRejectedCount":256,"latency":{"p90":0.0,"p99":1.0,"p50":0.0},"total":256},{"qps":[27,31,27,27,25,31,30,29,35,31],"commandHealth":"GREEN","commandName":"HighQPSCommand","circuitStatus":"CLOSED","timeoutCount":1,"successCount":852,"errorCount":0,"threadPoolRejectedCount":0,"latency":{"p90":31.0,"p99":35.0,"p50":20.0},"total":852},{"qps":[917,1200,1093,1197,1167,955,1062,962,948,913],"commandHealth":"GREEN","commandName":"HighLatencyCommand","circuitStatus":"CLOSED","timeoutCount":1,"successCount":25,"errorCount":0,"threadPoolRejectedCount":0,"latency":{"p90":1091.5,"p99":1200.0,"p50":822.0},"total":25}]}'
var data = JSON.parse(testData);