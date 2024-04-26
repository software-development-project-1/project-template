import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import QuizList from './QuizList';
import QuestionList from './QuestionList';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/quiz/:id" element={<QuestionList />} />
        <Route path="/" element={<QuizList />} />
      </Routes>
    </Router>
  );
}

export default App;
