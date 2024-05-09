import {
  Container,
  Box,
  Button,
  Typography,
} from "@mui/material";
import Rating from "@mui/material/Rating";

export default function BasicRating() {
  const reviews = [
    {
      username: "username1",
      date: "24.04.2024",
      rating: 4,
      comment: "Pretty good quiz",
    },
    {
      username: "username3",
      date: "05.03.2024",
      rating: 2,
      comment: "Not good quiz",
    },
  ];

  
  return (

      <Container>
        <h2>Reviews of the ...</h2>
        <Button variant="text">Write Your Review</Button>
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
            <h2>{review.username}</h2>
            <Typography>Written on {review.date}</Typography>
            <Typography component="legend">Rating {review.rating}/5</Typography>
            <Rating
              name={`read-only-${index}`}
              value={review.rating}
              readOnly
            />
            <Typography>{review.comment}</Typography>
          </Box>
        ))}
      </Container>
  );
}
