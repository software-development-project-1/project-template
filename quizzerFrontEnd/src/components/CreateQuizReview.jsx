import Box from "@mui/material/Box";
//import TextField from "@mui/material/TextField";
import {
  TextField,
  FormControl,
  FormLabel,
  FormControlLabel,
  Radio,
  RadioGroup,
  Button,
} from "@mui/material";

export default function CreateQuizReview() {
  return (
    <Box
      component="form"
      sx={{
        "& > :not(style)": { m: 1, width: "25ch" },
      }}
      noValidate
      autoComplete="off"
    >
      <h1>Add a review for ...</h1>
      <div>
        <TextField id="outlined-basic" label="Nickname" variant="outlined" />
      </div>
      <div>
        <FormControl>
          <FormLabel id="demo-radio-buttons-group-label">Rating</FormLabel>
          <RadioGroup
            aria-labelledby="demo-radio-buttons-group-label"
            name="radio-buttons-group"
          >
            <FormControlLabel
              value="1"
              control={<Radio />}
              label="1 - Useless"
            />
            <FormControlLabel value="2" control={<Radio />} label="2 - Poor" />
            <FormControlLabel value="3" control={<Radio />} label="3 - Ok" />
            <FormControlLabel value="4" control={<Radio />} label="4 - Good" />
            <FormControlLabel
              value="5"
              control={<Radio />}
              label="5 - Excellent"
            />
          </RadioGroup>
        </FormControl>
      </div>
      <div>
        <TextField id="outlined-basic" label="Review" variant="outlined" />
      </div>
      <Button variant="outlined">Sumbit Your Review</Button>
    </Box>
  );
}
