import {useState} from "react";
import JdAndCvUploader from "./component/JdAndCvUploader";
import QnA from "./component/QnA";
import Evaluation from "./component/Evaluation";
import Header from "./component/Header";
import Footer from "./component/Footer";
import './css/Chakri.css'

enum TChakriState {
    SUBMITTING_JD_AND_CV,
    SUBMITTING_QNA,
    VIEWING_EVALUATION
}

function Chakri() {
    const [chakriState, setChakriState] = useState<TChakriState>(TChakriState.SUBMITTING_JD_AND_CV);
    const [id, setId] = useState<string | null>('');

    const moveToQnA = (id: string) => {
        setId(id);
        setChakriState(TChakriState.SUBMITTING_QNA);
    }

    const moveToEvaluation = () => {
        setChakriState(TChakriState.VIEWING_EVALUATION);
    }

    const resetBackToJdAndCv = () => {
        setId(null);
        setChakriState(TChakriState.SUBMITTING_JD_AND_CV);
    }

    return (
        <div className="app-container">
            <Header />
            <div>
                {chakriState === TChakriState.SUBMITTING_JD_AND_CV && <JdAndCvUploader moveToQnA={moveToQnA} />}
                {chakriState === TChakriState.SUBMITTING_QNA && <QnA moveToEvaluation={moveToEvaluation} id={id as string} />}
                {chakriState === TChakriState.VIEWING_EVALUATION && <Evaluation resetBackToJdAndCv={resetBackToJdAndCv} id={id as string}/>}
            </div>
            <Footer />
        </div>
    );
}

export default Chakri;