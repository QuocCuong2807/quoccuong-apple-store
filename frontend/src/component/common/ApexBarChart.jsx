import React, { useState } from "react";
import ReactApexChart from "react-apexcharts";

function ApexBarChart({ data }) {
  console.log(data);
  const [state, setState] = useState({
    series: [
      {
        data: data.map(item => item.data),
      },
    ],
    options: {
      chart: {
        height: 350,
        type: "bar",
        events: {
          click: function (chart, w, e) {
            // console.log(chart, w, e)
          },
        },
      },
    //   colors: colors,
      plotOptions: {
        bar: {
          columnWidth: "45%",
          distributed: true,
        },
      },
      dataLabels: {
        enabled: false,
      },
      legend: {
        show: false,
      },
      xaxis: {
        // categories: [
        //   ["John", "Doe"],
        //   ["Joe", "Smith"],
        //   ["Jake", "Williams"],
        //   "Amber",
        //   ["Peter", "Brown"],
        //   ["Mary", "Evans"],
        //   ["David", "Wilson"],
        //   ["Lily", "Roberts"],
        // ],
        categories:data.map(item => [item.name]),
        labels: {
          style: {
            // colors: colors,
            fontSize: "12px",
          },
        },
      },
    },
  });
  return (
    <div className="w-full">
      <ReactApexChart
        options={state.options}
        series={state.series}
        type="bar"
      />
    </div>
  );
}

export default ApexBarChart;
