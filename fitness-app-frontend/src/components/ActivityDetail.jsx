import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { getActivityDetails, deleteRecAndActivity } from "../services/api";
import {
  Box,
  Card,
  CardContent,
  Divider,
  Typography,
  Button,
} from "@mui/material";

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [rec, setRec] = useState(null);
  const [recID, setRecID] = useState(null);
  const navigate = useNavigate();
  useEffect(() => {
    const fetchActivityDetails = async () => {
      try {
        const response = await getActivityDetails(id);
        setActivity(response.data);
        setRec(response.data.rec);
        setRecID(response.data.id);
      } catch (error) {
        console.error(error);
      }
    };
    fetchActivityDetails();
  }, [id]);

  const deleteItems = () => {
    try {
      deleteRecAndActivity(recID);
    } catch (error) {
      console.error(error);
    }
  };

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5">Activity Details: </Typography>
          <Typography>Type: {activity.activityType}</Typography>
          <Typography>
            Date: {new Date(activity.createdAt).toLocaleString()}
          </Typography>
        </CardContent>
      </Card>

      {rec && (
        <Card>
          <CardContent>
            <Typography variant="h5">AI RECOMMENDATIONS: </Typography>
            <Typography variant="h6">Analysis</Typography>
            <Typography>{activity.rec}</Typography>

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6">Improvements:</Typography>
            <Divider sx={{ my: 2 }} />
            {activity?.improvements?.map((improvement, index) => (
              <Typography key={index}>{improvement}</Typography>
            ))}
            <Divider sx={{ my: 2 }} />

            <Typography variant="h6">Suggestions:</Typography>
            <Divider sx={{ my: 2 }} />
            {activity?.suggestions?.map((sug, index) => (
              <Typography key={index}>{sug}</Typography>
            ))}
            <Divider sx={{ my: 2 }} />

            <Typography variant="h6">Safety Guidelines:</Typography>
            <Divider sx={{ my: 2 }} />
            {activity?.safety?.map((saf, index) => (
              <Typography key={index}>{saf}</Typography>
            ))}
          </CardContent>
        </Card>
      )}
      <Button
        variant="contained"
        color="secondary"
        sx={{ mt: 2 }}
        onClick={() => navigate("/activities")}
      >
        Back To Activity List
      </Button>
      <Button
        color="error"
        variant="contained"
        sx={{ mt: 2, ml: 5 }}
        onClick={() => {
          deleteItems();
          navigate("/activities");
        }}
      >
        DELETE ACTIVITY
      </Button>
    </Box>
  );
};

export default ActivityDetail;
