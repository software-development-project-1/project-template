import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import QuizList from './QuizList';
import QuestionList from './QuestionList';
import Results from './Results';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/quiz/:id" element={<QuestionList />} />
        <Route path="/" element={<QuizList />} />
        <Route path="/quiz/:id/answers" element={<Results />} />
      </Routes>
    </Router>
  );
}

export default App;
