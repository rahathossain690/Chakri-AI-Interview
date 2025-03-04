import React, { useState, useEffect, useRef } from 'react';
import { IQuestions, getQnA, getDefaultAnswer, sendAnswer} from '../service/apiService';
import { showError, withConfimration } from '../service/dialogService';
import '../css/QnA.css';

interface QnAProps {
  id: string;
  moveToEvaluation: (id: string) => void;
}

function QnA({ id, moveToEvaluation }: QnAProps) {

    const [questions, setQuestions] = useState<IQuestions>({ ready: false, questions: [] });
    const [answers, setAnswers] = useState<string[]>([]);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const intervalId = useRef<NodeJS.Timeout | null>(null);

    useEffect(() => {
        const fetchData = async () => {
          try {
            const result = await getQnA(id);
            setQuestions(result);

            if (result.ready) {
              clearInterval(intervalId.current!);
              setAnswers(Array(result.questions.length).fill(''));
            }

          } catch (error: any) {
            showError(error?.message || "Cannot fetch questions");
            clearInterval(intervalId.current!);
          }
        };
    
        intervalId.current = setInterval(fetchData, 10000);
    
        return () => {
          if (intervalId.current) {
            clearInterval(intervalId.current);
          }
        };
      }, [id]);
  
    const handleAnswerChange = (index: number, value: string) => {
      const newAnswers = [...answers];
      newAnswers[index] = value;
      setAnswers(newAnswers);
    };

    const onSubmitForm = async (event: React.FormEvent) => {
        if (await withConfimration()) {
            handleSubmit(event);
        }
    }
  
    const handleSubmit = (event: React.FormEvent) => {
      event.preventDefault();
      setIsSubmitting(true);
  
      const processedAnswers = questions.questions.map((question, index) => {
        if (answers[index] === '') {
          return getDefaultAnswer();
        }
        if (question.type === 'Multiple Choice' && question.options.length > 0) {
          return question.options[parseInt(answers[index])];
        }
        return answers[index];
      });

      sendAnswer(id, processedAnswers)
        .then(() => {
          moveToEvaluation(id);
        })
        .catch((error) => {
          showError(error?.message || "Cannot submit answers");
          setIsSubmitting(false);
        });
    };
  
    if (!questions.ready) {
      return <form className='questions-form'>Loading questions...</form>;
    }
  
    return (
      <form className="questions-form">
        {questions.questions.map((question, index) => (
          <div key={index} className="question-group">
            <label className="question-label">{question.question}</label>
            {question.type === 'Paragraph' ? (
              <textarea
                value={answers[index]}
                onChange={(e) => handleAnswerChange(index, e.target.value)}
                className="question-textarea"
              />
            ) : (
              <select
                value={answers[index]}
                onChange={(e) => handleAnswerChange(index, e.target.value)}
                className="question-select"
              >
                <option value="">Select an option</option>
                {question.options.map((option, optionIndex) => (
                  <option key={optionIndex} value={optionIndex}>
                    {option}
                  </option>
                ))}
              </select>
            )}
          </div>
        ))}
        <button type='button' disabled={isSubmitting} className="submit-button" onClick={(e) => onSubmitForm(e)}>
          Submit
        </button>
      </form>
    );
  }
  
  export default QnA;