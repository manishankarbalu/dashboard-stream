import React, { Component } from 'react';
import CommandGrid from "./CommandGrid";
import { getInitialMetricsData } from './DummyDataProvider';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: getInitialMetricsData()
        };
        this.eventSource = new EventSource('http://127.0.0.1:8080/dashboard.stream');
    }

    commandMessageListner = e => {
        // console.log(e.data)
        this.refreshCommandData(JSON.parse(e.data))
    };

    errorListner = e => {
        if (e.eventPhase == EventSource.CLOSED) {
            console.log("Connection was closed on error: " + e);
            this.eventSource.close()
        } else {
            console.log("Error occurred while streaming: " + e);
            this.eventSource.close()
        }
    }

    componentDidMount() {
        this.eventSource.addEventListener('message', this.commandMessageListner, false);
        this.eventSource.addEventListener('error', this.errorListner, false);
    }

    refreshCommandData(updatedCommandMetrics) {
        this.setState(Object.assign({}, { data: updatedCommandMetrics }));
    }

    render() {
        return (<CommandGrid data={this.state.data} />);
    }
}

export default App;