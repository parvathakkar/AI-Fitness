import { Box, Button, Typography } from "@mui/material";
import { useState, useContext, useEffect } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import SplitText from "./SplitText/SplitText";
import {
  BrowserRouter as Router,
  Navigate,
  Route,
  Routes,
  useLocation,
} from "react-router";
import { setCredentials } from "./store/authSlice";
import ActivityList from "./components/ActivityList";
import ActivityForm from "./components/ActivityForm";
import ActivityDetail from "./components/ActivityDetail";

const ActivitiesPage = () => {
  return (
    <Box sx={{ display: "flex", flexDirection: "column" }}>
      <ActivityForm onActivitiesAdded={() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
};

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } =
    useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
      console.log("User authenticated");
    }
  }, [token, tokenData, dispatch]);
  return (
    <Router>
      {!token ? (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            minHeight: "90vh",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <SplitText
            text="Welcome To The New Age Of AI Powered Fitness"
            className="text-3xl font-semibold text-center"
            delay={100}
            duration={0.6}
            ease="power3.out"
            splitType="chars"
            from={{ opacity: 0, y: 40 }}
            to={{ opacity: 1, y: 0 }}
            tag="h1"
            threshold={0.1}
            rootMargin="-100px"
            textAlign="center"
          />
          <Button
            variant="contained"
            color="success"
            onClick={() => logIn()}
            size="large"
            sx={{ mt: 5 }}
          >
            Log In
          </Button>
        </Box>
      ) : (
        <Box>
          <Box>
            <Button variant="contained" onClick={() => logOut()}>
              LOG OUT
            </Button>
          </Box>
          <Routes>
            <Route path="/activities" element={<ActivitiesPage />} />
            <Route path="/activity/:id" element={<ActivityDetail />} />
            <Route
              path="/"
              element={
                token ? (
                  <Navigate to="/activities" replace />
                ) : (
                  <div>Welcome, Please Log in</div>
                )
              }
            />
          </Routes>
        </Box>
      )}
    </Router>
  );
}

export default App;
