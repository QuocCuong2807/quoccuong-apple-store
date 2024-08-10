import React from "react";

import ReactApexChart from "react-apexcharts";

function IncomeAnalyze({ OnRevenueFilterChange, data }) {
  const barChart = {
    series: [
      {
        data: data.map((item) => item.data),
      },
    ],
    options: {
      chart: {
        height: 350,
        type: "bar",
        // events: {
        //   click: function (chart, w, e) {
        //     // console.log(chart, w, e)
        //   },
        // },
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
        show: true,
      },
      xaxis: {
        categories: data.map((item) => [item.name]),
        labels: {
          style: {
            fontSize: "12px",
            
          },
        },
      },
    },
  };

  const pieChart = {
    series: data.map((item) => item.data),
    options: {
      chart: {
        width: 380,
        type: "pie",
      },
      labels: data.map((item) => [item.name]),
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
            },
            legend: {
              position: "bottom",
            },
          },
        },
      ],
    },
  };

  return (
    <div>
      <div className="flex justify-between items-center px-6 py-4">
        <h1 className="font-bold font-mono text-lg">Biểu đồ tổng doanh thu:</h1>
        {/* Filter */}
        <div class="max-w-sm ">
          <select
            id="countries"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
             focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400
              dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 hover:cursor-pointer"
            onChange={(e) => OnRevenueFilterChange(e)}
          >
            <option value="TOTAL_REVENUE">Tông doanh thu</option>
            <option value="CRR_MONTH">Trong tháng</option>
            <option value="CRR_WEEK">Trong tuần</option>
            <option value="LAST_WEEK">Tuần trước</option>
          </select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6 px-6 pb-6">
        <div className="border border-slate-400 col-span-1 flex justify-center items-center rounded-lg shadow-xl">
          <div className="w-full">
            <ReactApexChart
              options={barChart.options}
              series={barChart.series}
              type="bar"
            />
          </div>
        </div>
        <div className="border border-slate-400 col-span-1 flex justify-center items-center  rounded-lg shadow-xl">
          <div className="w-full">
            <ReactApexChart
              options={pieChart.options}
              series={pieChart.series}
              type="pie"
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default IncomeAnalyze;
