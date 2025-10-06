import React from "react";
import { useState } from "react";
import { addActivity } from "../services/api";
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";

const ActivityForm = ({ onActivitiesAdded }) => {
  const [activity, setActivity] = useState({
    activityType: "RUNNING",
    duration: "",
    calories: "",
    additionalMetrics: {},
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addActivity(activity);
      onActivitiesAdded();
      setActivity({ activityType: "RUNNING", duration: "", calories: "" });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      sx={{
        display: "flex",
        flexDirection: "column",
        minHeight: "90vh",
        justifyContent: "center",
        alignItems: "center",
        mb: -15,
      }}
    >
      <FormControl sx={{ mb: 5 }}>
        <InputLabel>Activity Type</InputLabel>
        <Select
          value={activity.activityType}
          onChange={(e) => {
            setActivity({ ...activity, activityType: e.target.value });
          }}
        >
          <MenuItem value="RUNNING">Running</MenuItem>
          <MenuItem value="WALKING">Walking</MenuItem>
          <MenuItem value="CYCLING">Cycling</MenuItem>
        </Select>
      </FormControl>
      <TextField
        label="Duration (mins)"
        type="number"
        sx={{ mb: 4 }}
        value={activity.duration}
        onChange={(e) => {
          setActivity({ ...activity, duration: e.target.value });
        }}
      ></TextField>
      <TextField
        label="Calories Burned"
        type="number"
        sx={{ mb: 2 }}
        value={activity.calories}
        onChange={(e) => {
          setActivity({ ...activity, calories: e.target.value });
        }}
      ></TextField>
      <Button type="submit" variant="contained">
        Add Activity
      </Button>
    </Box>
  );
};

export default ActivityForm;
