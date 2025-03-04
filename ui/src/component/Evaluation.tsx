import {useState, useEffect, useRef, JSX} from 'react';
import { showError } from '../service/dialogService';
import {IEvaluation, getEvaluation} from '../service/apiService'
import '../css/Evaluation.css';

function EvaluationScoreComponent({id, resetBackToJdAndCv}: {id: string, resetBackToJdAndCv: () => void}): JSX.Element {

    const [evaluation, setEvaluation] = useState<IEvaluation>({ready: false});
    const intervalId = useRef<NodeJS.Timeout | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
              const result = await getEvaluation(id);
              setEvaluation(result);
  
              if (result.ready) {
                clearInterval(intervalId.current!);
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

    if (!evaluation.ready) {
        return <div className="loading-evaluation">Loading evaluation...</div>;
    }

    const scoreItems = [
        { label: 'Overall Score', value: evaluation.evaluationScore?.score || 0 },
        { label: 'Relevance to JD', value: evaluation.evaluationScore?.relevanceToJd || 0 },
        { label: 'Technical Proficiency', value: evaluation.evaluationScore?.technicalProficiency || 0 },
        { label: 'Communication Skills', value: evaluation.evaluationScore?.communicationSkills || 0 },
        { label: 'Problem Solving Ability', value: evaluation.evaluationScore?.problemSolvingAbility || 0 },
        { label: 'Experience', value: evaluation.evaluationScore?.experience || 0 },
        { label: 'Achievement', value: evaluation.evaluationScore?.achievement || 0 },
        { label: 'Cultural and Soft Fit', value: evaluation.evaluationScore?.culturalAndSoftFit || 0 },
    ];

    return (
        <div className="evaluation-container">
            <h2 className="evaluation-title">Evaluation Scores</h2>
            <div className="score-grid">
                {scoreItems.map((item) => (
                <div key={item.label} className="score-item">
                    <span className="score-label">{item.label}:</span>
                    <span className="score-value">{item.value}</span>
                    <div className="score-bar-container">
                    <div
                        className="score-bar"
                        style={{ width: `${item.value}%` }}
                    ></div>
                    </div>
                </div>
                ))}
            </div>
            <div>
              <button type="submit" className="submit-button" style={{width: "100%"}} onClick={resetBackToJdAndCv}>
                Thank You
              </button>
            </div>
        </div>
    );
}

export default EvaluationScoreComponent;