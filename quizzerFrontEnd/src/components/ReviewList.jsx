import { useState, useEffect, useRef } from "react";
import { Container, Box, Button, Typography } from "@mui/material";
import Rating from "@mui/material/Rating";
import { Link } from "react-router-dom";

function ReviewList({ quizId }) {
  const [reviews, setReviews] = useState([]);
  const gridRef = useRef(null);

  const fetchReviews = async (id) => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}/reviews`
      );
      if (!response.ok) {
        throw new Error("Failed to fetch reviews");
      }
      const data = await response.json();
      setReviews(data);
    } catch (error) {
      console.error("Error fetching reviews: ", error);
    }
  };

  useEffect(() => {
    fetchReviews(quizId);
  }, [quizId]);

  return (
    <Container>
      <Typography variant="h4">Reviews of the Quiz</Typography>
      <Link to="/create-review">
        <Button variant="text">Write Your Review</Button>
      </Link>
      {reviews.map((review, index) => (
        <Box
          key={index}
          sx={{
            marginBottom: "20px",
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
            borderRadius: "10px",
            padding: "20px",
          }}
        >
          <Typography variant="h6">{review.username}</Typography>
          <Typography variant="body2">
            Written on {new Date(review.createdAt).toLocaleDateString()}
          </Typography>
          <Typography variant="body2" component="legend">
            Rating {review.rating}/5
          </Typography>
          <Rating name={`read-only-${index}`} value={review.rating} readOnly />
          <Typography variant="body1">{review.review}</Typography>
        </Box>
      ))}
    </Container>
  );
}

export default ReviewList;
