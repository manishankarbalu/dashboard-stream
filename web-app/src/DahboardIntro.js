import './stylesheet.css';
import logo from './assets/icon.png'

function DashbaordIntro() {
    return (<div class="heading">
        <img src={logo} />
        <div class="headingText">StreamDashboard</div>
    </div>);
}
export default DashbaordIntro; 