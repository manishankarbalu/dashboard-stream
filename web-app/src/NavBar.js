import './stylesheet.css';

function NavBar() {
    return (<div class="nav">
        <div class="sortFunc">
            <div>Sort: </div>
            <a class="sortOption column">Error then volume</a>
            <a class="sortOption column">Alphabetical</a>
            <a class="sortOption column">Volume</a>
            <a class="sortOption column">Error</a>
            <a class="sortOption column">99.5</a>
        </div>
        <div class="legend">
            <div class="legendOption column successColor">Success</div>
            <div class="legendOption column circuitColor">Short-Circuited</div>
            <div class="legendOption column timeoutColor">Timeout</div>
            <div class="legendOption column threadPoolRejectedColor">Rejected</div>
            <div class="legendOption column errorColor">Failure</div>
        </div>
    </div>
    );
}
export default NavBar;