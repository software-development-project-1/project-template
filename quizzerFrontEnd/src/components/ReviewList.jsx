import { useEffect, useState } from 'react';
import { Button, Typography, Box } from "@mui/material";
import Rating from "@mui/material/Rating";
import { Link, useParams } from "react-router-dom";


function ReviewList() {
  const { id } = useParams();
  const [quizName, setQuizName] = useState('');
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchQuizDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/QuizApp/quiz/${id}`);
        if (!response.ok) {
          throw new Error('Failed to fetch quiz details');
        }
        const data = await response.json();
        setQuizName(data.quizName);
      } catch (error) {
        console.error('Error fetching quiz details: ', error);
      }
    };

    const fetchReviews = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/QuizApp/quiz/${id}/reviews`);
        if (!response.ok) {
          throw new Error('Failed to fetch reviews');
        }
        const data = await response.json();
        setReviews(data);
        setLoading(false); 
      } catch (error) {
        console.error('Error fetching reviews: ', error);
      }
    };
    fetchQuizDetails();
    fetchReviews();
  }, [id]);

  return (
    <div>
      <h2>Reviews of Quiz &quot;{quizName}&quot;</h2>
      <div>
        <Button variant="text" component={Link} to={`/quiz/${id}/reviews/${id}`} size="large">Write Your Review</Button>
        <Button variant="text" component={Link} to={`/`} color="error" size="large">Go Back</Button>    
      </div>
      
      {loading ? (
        <p>Loading reviews...</p>
      ) : reviews.length === 0 ? (
        <p>No reviews for this quiz.</p>
      ) : (
        reviews.map((review, index) => (
          <Box
            key={index}
            sx={{
              marginBottom: "20px",
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
              borderRadius: "10px", 
              padding: "20px", 
            }}
          >
            <Typography variant="h6">Username: {review.username}</Typography>
            <Typography variant="body1">Written on {review.date}</Typography>
            <Typography variant="body1">Rating: {review.rating}/5</Typography>
            <Rating
              name={`read-only-${index}`}
              value={review.rating}
              readOnly
            />
            <Typography variant="body1">Comment: {review.review}</Typography>
          </Box>
        ))
      )}
    </div>
  );
}

export default ReviewList;
