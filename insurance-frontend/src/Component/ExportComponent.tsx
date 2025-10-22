import { Button, Group } from '@mantine/core'
import { IconFileSpreadsheet, IconFileText } from '@tabler/icons-react'
import axios from 'axios';

interface ResultTableProps {
  data: any[];
}


const ExportComponent: React.FC<ResultTableProps> = ({ data }) => {
    const handleExport = async (type: "excel" | "pdf") => {
    try {
      const response = await axios.post(
        `http://localhost:8080/api/insurance/export/${type}`,
        data, // send your current table data
        { responseType: "blob" } // important for file download
      );

      const blob = new Blob([response.data], {
        type: type === "excel"
          ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
          : "application/pdf"
      });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `Insurance_Report.${type === "excel" ? "xlsx" : "pdf"}`;
      link.click();
    } catch (error) {
      console.error("Export failed:", error);
    }
  };
  return (
    <div className='mt-5'>
         <Group justify="center" mb="lg">
        <Button
          leftSection={<IconFileSpreadsheet size={18} />}
          color="green"
          onClick={() => handleExport("excel")}
        >
          Export Excel
        </Button>
        <Button
          leftSection={<IconFileText size={18} />}
          color="red"
          onClick={() => handleExport("pdf")}
        >
          Export PDF
        </Button>
      </Group>
    </div>
  )
}

export default ExportComponent