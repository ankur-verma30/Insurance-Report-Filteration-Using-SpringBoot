import { Select, Button, Notification } from "@mantine/core";
import { DateInput } from '@mantine/dates';
import { useState} from "react";
import { useForm } from "@mantine/form";
import axios from "axios";
import { planName, planStatus } from "./data";
import { IconX, IconCheck } from '@tabler/icons-react';
import ResultTable from "./ResultTable";
import { showNotification } from "@mantine/notifications";

interface Query {
  planName?: string;
  planStatus?: string;
  gender?: string;
  planStartDate?: Date | null;
  planEndDate?: Date | null;
}

const SearchComponent = () => {
     const xIcon = <IconX size={20} />;
  const checkIcon = <IconCheck size={20} />;
  const [results, setResults] = useState([]);

  const form = useForm({
    initialValues: {
      planName: "",
      planStatus: "",
      gender: "",
      planStartDate: null,
      planEndDate: null,
    },
  });

  // Fetch data whenever query changes

 const handleSubmit = async (values: Query) => {
  try {
    const params: Record<string, any> = {}; // create empty object first

    if (values.planName) params.planType = values.planName;
    if (values.planStatus) params.planStatus = values.planStatus;
    if (values.gender) params.gender = values.gender;
    if (values.planStartDate)
      params.planStartDate = values.planStartDate.toISOString().split("T")[0];
    if (values.planEndDate)
      params.planEndDate = values.planEndDate.toISOString().split("T")[0];

    const response = await axios.get(
      "http://localhost:8080/api/insurance/search",
      { params }
    );

    if(response.data.length>0){
        setResults(response.data);
   showNotification({
          title: "Fetch Successful ✅",
          message: "Data fetched successfully!",
          color: "green",
          icon: <IconCheck size={18} />,
        });
      } else {
        setResults([]);
        showNotification({
          title: "No Data Found ⚠️",
          message: "No matching records found.",
          color: "yellow",
          icon: <IconX size={18} />,
        });
      }
    } catch (error) {
      console.error("Error fetching data:", error);
      showNotification({
        title: "Error ❌",
        message: "Something went wrong while fetching data.",
        color: "red",
        icon: <IconX size={18} />,
      });
    }
  };


  return (
    <div className="flex flex-col items-center justify-center bg-blue-100 px-6 space-y-10  ">
      <form
        onSubmit={form.onSubmit(handleSubmit)}
        className="bg-white p-8 rounded-2xl shadow-lg w-full max-w-md space-y-6"
      >
        <h2 className="bg-blue-400 text-2xl p-8 rounded-2xl shadow-lg w-full max-w-md space-y-6 text-center">
          Insurance Report Filter
        </h2>

        <Select
          {...form.getInputProps("planName")}
          data={planName}
          label="Plan Name"
          placeholder="Pick one"
          radius="md"
        />

        <Select
          {...form.getInputProps("planStatus")}
          data={planStatus}
          label="Plan Status"
          placeholder="Pick one"
          radius="md"
          searchable
        />

        <Select
          {...form.getInputProps("gender")}
          data={["Male", "Female"]}
          label="Gender"
          placeholder="Pick Gender"
          radius="md"
        />

        <DateInput
          {...form.getInputProps("planStartDate")}
          label="Start Date"
          placeholder="Pick Startdate"
          radius="md"
        />

        <DateInput
          {...form.getInputProps("planEndDate")}
          label="End Date"
          placeholder="Pick End date"
          radius="md"
        />

        <Button
          type="submit"
          fullWidth
          radius="md"
          className="bg-cyan-600 hover:bg-cyan-700 text-white font-semibold py-2 rounded-lg transition duration-200"
        >
          Generate Report
          
        </Button>
      </form>
    
 {
    results.length>0 && (
        <div className="w-full max-w-screen mt-6">
      <ResultTable data={results} />
    </div>
    )
  }
    </div>
    );
};

export default SearchComponent;
