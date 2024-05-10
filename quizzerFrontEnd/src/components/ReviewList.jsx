import { useEffect, useState } from "react";
import { Button, Typography, Box } from "@mui/material";
import Rating from "@mui/material/Rating";
import { Link, useParams } from "react-router-dom";

function ReviewList() {
  const { id } = useParams();
  const [quizName, setQuizName] = useState("");
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchQuizDetails = async () => {
      try {
        const response = await fetch(
          `${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}`
        );
        if (!response.ok) {
          throw new Error("Failed to fetch quiz details");
        }
        const data = await response.json();
        setQuizName(data.quizName);
      } catch (error) {
        console.error("Error fetching quiz details: ", error);
      }
    };

    const fetchReviews = async () => {
      try {
        const response = await fetch(
          `${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}/reviews`
        );
        if (!response.ok) {
          throw new Error("Failed to fetch reviews");
        }
        const data = await response.json();
        setReviews(data);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching reviews: ", error);
      }
    };
    fetchQuizDetails();
    fetchReviews();
  }, [id]);

  return (
    <div>
      <h2>Reviews of Quiz &quot;{quizName}&quot;</h2>

      <div style={{ display: "flex", justifyContent: "center" }}>
        <Button
          variant="text"
          component={Link}
          to={`/quiz/${id}/reviews/${id}`}
          sx={{ fontSize: "1.5rem" }}
        >
          Write Your Review
        </Button>
        <Button
          variant="text"
          component={Link}
          to={`/`}
          color="error"
          sx={{ fontSize: "1.5rem" }}
          style={{ marginLeft: "10px" }}
        >
          Go Back
        </Button>
      </div>
      {loading ? (
        <div style={{ margin: "30px" }}>
          <h4>Loading reviews...</h4>
        </div>
      ) : reviews.length === 0 ? (
        <h4
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          No reviews for this quiz...
        </h4>
      ) : (
        reviews.map((review, index) => (
          <Box
            key={index}
            sx={{
              margin: "20px 30px",
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
              borderRadius: "10px",
              padding: "20px",
            }}
          >
            <Typography variant="h4">Username: {review.username}</Typography>
            <Typography variant="h5">
              Written on{" "}
              {new Date(review.createdAt).toLocaleDateString("en-GB")}
            </Typography>
            <Typography variant="h5">Rating: {review.rating}/5</Typography>
            <Rating
              name={`read-only-${index}`}
              value={review.rating}
              size="large"
              readOnly
            />
            <Typography variant="h5">Comment: {review.review}</Typography>
          </Box>
        ))
      )}
    </div>
  );
}

export default ReviewList;
