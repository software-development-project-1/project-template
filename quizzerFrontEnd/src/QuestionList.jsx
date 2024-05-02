import { useState, useEffect, useRef } from "react";
import { useParams } from 'react-router-dom';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import TextField from '@mui/material/TextField';
import Chip from '@mui/material/Chip';
import Snackbar from '@mui/material/Snackbar';
import { Container, Box, FormControl, InputLabel, Select, MenuItem } from '@mui/material';

function QuestionList() {
    const [questionList, setQuestionList] = useState([]);
    const [answer, setAnswer] = useState({});
    const [inputAnswers, setInputAnswers] = useState({});
    const [correctAnswer, setCorrectAnswer] = useState('');
    const [open, setOpen] = useState(false);
    const [correct, setCorrect] = useState(false);
    const [selectedDifficulty, setSelectedDifficulty] = useState('');
    const { id } = useParams();
    const [currentQuestionId, setCurrentQuestionId] = useState(null); // New state to store the ID of the current question being answered

    useEffect(() => {
        fetchQuestionList();
    }, [id]);

    const fetchQuestionList = async () => {
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}/questions`);
            if (!response.ok) {
                throw new Error("Error in retrieving questions" + response.statusText);
            }
            const data = await response.json();
            setQuestionList(data);
        } catch (error) {
            console.error("Failed to load questions: ", error);
        }
    };

    const fetchAnswer = async (questionId) => {
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/questions/${questionId}/answer`);
            if (!response.ok) {
                throw new Error("Error in retrieving question answer" + response.statusText);
            }
            console.log(response);
            const data = await response.json();
            console.log('response data: ', data);
            setCorrectAnswer(data.answerText); // Update the correct answer state

            // Check the user's answer against the correct answer
            const userAnswer = inputAnswers[questionId]?.trim().toLowerCase();
            const correctAnswerText = data.answerText.trim().toLowerCase();
            if (userAnswer === correctAnswerText) {
                setCorrect(true);
            } else {
                setCorrect(false);
            }

            setOpen(true); // Open the snackbar after checking the answer
        } catch (error) {
            console.error("Failed to load question answer: ", error);
        }
    }
    const postAnswer = async (questionId, userAnswer) => {
        const answerDto = {
            answerText: userAnswer,
            questionId: questionId
        };
    
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/questions/${questionId}/answers`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(answerDto)
            });
    
            if (!response.ok) {
                throw new Error('Failed to post answer');
            }
            const result = await response.json();
            console.log('Posted answer:', result);
            
        } catch (error) {
            console.error('Error posting answer:', error);
        }
    }


    const handleSubmitAnswer = (questionId) => {
        const userAnswer = inputAnswers[questionId]?.trim();
        if (!userAnswer) {
            console.error("No answer provided for question ID:", questionId);
            return;
    }

        fetchAnswer(questionId);
        postAnswer(questionId, userAnswer);
        setCurrentQuestionId(questionId); // Set the current question ID before comparing answers
    }

    const handleCheckAnswer = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    const handleDifficultyChange = (event) => {
        setSelectedDifficulty(event.target.value);
    };

    const filteredQuestions = questionList.filter(question => {
        if (selectedDifficulty === '') {
            return true; // Show all questions if no difficulty selected
        }
        return question.difficultyLevel.toLowerCase() === selectedDifficulty.toLowerCase();
    });

    return (
        <Container maxWidth="md" style={{ marginTop: '20px' }}>
            <Typography variant="h2" component="h1" gutterBottom align="center">
                {questionList.length > 0 ? questionList[0].quiz.quizName : ''}
            </Typography>
            <Typography variant="h4" component="h2" gutterBottom align="center" color="textSecondary">
                {questionList.length > 0 ? questionList[0].quiz.quizDescription : ''}
            </Typography>
            <Box sx={{ marginBottom: '20px' }}>
                <FormControl sx={{ minWidth: 120 }}>
                    <InputLabel id="difficulty-level-label">Difficulty Level</InputLabel>
                    <Select
                        labelId="difficulty-level-label"
                        id="difficulty-level-select"
                        value={selectedDifficulty}
                        label="Difficulty Level"
                        onChange={handleDifficultyChange}
                    >
                        <MenuItem value="">All</MenuItem>
                        <MenuItem value="easy">Easy</MenuItem>
                        <MenuItem value="normal">Normal</MenuItem>
                        <MenuItem value="hard">Hard</MenuItem>
                    </Select>
                </FormControl>
            </Box>
            <div>
                {filteredQuestions.map((question, index) => (
                    <Accordion key={index}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls={`panel${index}a-content`}
                            id={`panel${index}a-header`}
                        >
                            <Box>
                                <Typography variant="h5">{question.questionText}</Typography>
                                <Typography variant="subtitle1" gutterBottom>Difficulty Level:
                                    <Chip label={` ${question.difficultyLevel}`} size="medium" color="success" />
                                </Typography>
                            </Box>
                        </AccordionSummary>
                        <AccordionDetails>
                            <TextField
                                fullWidth
                                label="Your Answer"
                                variant="outlined"
                                placeholder="Type your answer here"
                                value={inputAnswers[question.questionId] || ''}
                                onChange={e => setInputAnswers({ ...inputAnswers, [question.questionId]: e.target.value })}
                            />
                            <Typography
                                variant="body2"
                                onClick={() => handleSubmitAnswer(question.questionId)}
                                style={{ cursor: 'pointer', color: 'blue', marginTop: '8px' }}
                            >
                                SUBMIT YOUR ANSWER
                            </Typography>
                        </AccordionDetails>
                    </Accordion>
                ))}
            </div>

            <Snackbar
                open={open}
                autoHideDuration={6000}
                onClose={handleCheckAnswer}
                message={correct ? "This is correct, good job!" : `That is not correct the correct answer is '${correctAnswer}' !`}
            />
        </Container>
    );
}

export default QuestionList;
