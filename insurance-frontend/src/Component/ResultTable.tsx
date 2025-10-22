import React from "react";
import { Table, Text, Paper, Center } from "@mantine/core";
import ExportComponent from "./ExportComponent";

interface ResultTableProps {
  data: any[];
}

const ResultTable: React.FC<ResultTableProps> = ({ data }) => {
  const hasData = data && data.length > 0;

  return (
    <Paper shadow="md" radius="lg" p="xl" withBorder>
      <Center>
        <Text size="xl" fw={700} py="md" px="md" className="bg-blue-400 text-white rounded-full">
          Report Details
        </Text>
      </Center>

      <Table
        striped
        highlightOnHover
        withTableBorder
        withColumnBorders
        mt="lg"
        horizontalSpacing="md"
        verticalSpacing="sm"
        
      >
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Plan Name</Table.Th>
            <Table.Th>Plan Status</Table.Th>
            <Table.Th>Gender</Table.Th>
            <Table.Th>Start Date</Table.Th>
            <Table.Th>End Date</Table.Th>
          </Table.Tr>
        </Table.Thead>

        <Table.Tbody>
          {hasData ? (
            data.map((item, index) => (
              <Table.Tr key={index}>
                <Table.Td>{item.planType}</Table.Td>
                <Table.Td>{item.planStatus}</Table.Td>
                <Table.Td>{item.gender}</Table.Td>
                <Table.Td>{item.planStartDate}</Table.Td>
                <Table.Td>{item.planEndDate}</Table.Td>
              </Table.Tr>
            ))
          ) : (
            <Table.Tr>
              <Table.Td colSpan={5}>
                <Center>
                  <Text c="red" size="lg" fw={600}>
                    No Data Found
                  </Text>
                </Center>
              </Table.Td>
            </Table.Tr>
          )}
        </Table.Tbody>
      </Table>
      <ExportComponent data={data} />
    </Paper>
  );
};

export default ResultTable;
