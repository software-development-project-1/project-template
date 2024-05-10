import { useState } from 'react';
import Box from "@mui/material/Box";
import {
  TextField,
  FormControl,
  FormLabel,
  FormControlLabel,
  Radio,
  RadioGroup,
  Button,
} from "@mui/material";
import axios from 'axios'; 
import { Link, useParams  } from 'react-router-dom';


const CreateQuizReview = () => {

  const { id } = useParams();
  
  const [nickname, setNickname] = useState('');
  const [rating, setRating] = useState('');
  const [reviewText, setReviewText] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    try {
      const newReview = {
        username: nickname, 
        rating: rating,
        review: reviewText
      };

      const response = await axios.post(`${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}/review`, newReview);

      if (response.status === 201) {
        setNickname('');
        setRating('');
        setReviewText(''); 
        // Redirect to the reviews page for the current quiz
        window.location.href = `/quiz/${id}/reviews`;
      } else {
        console.error('Failed to create review:', response.data);
      }
    } catch (error) {
      console.error('Error submitting review:', error);
    }
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit} 
      sx={{
        "& > :not(style)": { m: 2, width: "25ch"},
      }}
      noValidate
      autoComplete="off"
    >
      <h1>Add your review here</h1>
      <div>
        <TextField 
          id="nickname" 
          label="Nickname" 
          variant="outlined" 
          value={nickname} 
          onChange={(e) => setNickname(e.target.value)} 
        />
      </div>
      <div>
        <FormControl>
          <FormLabel id="demo-radio-buttons-group-label" style={{ fontSize: '1.5rem' }}>Rating</FormLabel>
          <RadioGroup
            aria-labelledby="demo-radio-buttons-group-label"
            name="rating"
            value={rating}
            onChange={(e) => setRating(e.target.value)} 
          >
            <FormControlLabel value="1"  control={<Radio />} label="1 - Useless" />
            <FormControlLabel value="2" control={<Radio />} label="2 - Poor" />
            <FormControlLabel value="3" control={<Radio />} label="3 - Ok" />
            <FormControlLabel value="4" control={<Radio />} label="4 - Good" />
            <FormControlLabel value="5" control={<Radio />} label="5 - Excellent" />
          </RadioGroup>
        </FormControl>
      </div>
      <div>
        <TextField
          id="review"
          label="Review"
          variant="outlined"
          multiline
          rows={4}
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
        />
      </div>
      <Button type="submit" variant="outlined">Submit Review</Button>
      <Button variant="outlined" component={Link} to={`/quiz/${id}/reviews`} color="error">Go Back</Button> 
    </Box>
  );
};

export default CreateQuizReview;
