import CommandComponent from "./CommandComponent";

function CommandGrid(prop) {
  // console.log(prop.data);
  return (<div className="containerGrid" style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gridGap: '10px', gridAutoRows: 'minMax(100px, auto)' }}>
    {prop.data.commandMetricsData.map((commandMetric) => (
      <div>
        <CommandComponent data={commandMetric} />
      </div>
    ))}
  </div>);
}
export default CommandGrid;