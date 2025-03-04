
function getApiUrl() {
  return "http://localhost:8080";
}

export async function submitJdAndCv(jdFile: File, cvFile: File): Promise<string> {

    const formData = new FormData();
    formData.append("jd", jdFile);
    formData.append("cv", cvFile);

    try {
        const result = await fetch(`${getApiUrl()}/chakri/jd-and-cv`, {
            method: "POST",
            body: formData,
        });

        if (!result.ok) {
            const errorResponse = await result.json();
            throw new Error(errorResponse?.error || "Cannot submit!");
        }

        return (await result.json()).id;

    } catch (error: any) {
        throw new Error(error.message || "Cannot submit!");
    }
}

export interface IQuestion {
    question: string;
    type: "Paragraph" | "Multiple Choice";
    options: string[];
}

export interface IQuestions {
    ready: boolean;
    questions: IQuestion[];
}

export async function getQnA(id: string): Promise<IQuestions> {

    try {
        const result = await fetch(`${getApiUrl()}/chakri/question/${id}`);
        if (!result.ok) {
            const errorResponse = await result.json();
            throw new Error(errorResponse?.error || "Cannot submit!");
        }

        return await result.json();
    } catch (error: any) {
        throw new Error(error.message || "Cannot fetch questions");
    }
}

export function getDefaultAnswer(){
    return "Not answered";
}

export async function sendAnswer(id: string, answers: string[]) {
    try {
        const result = await fetch(`${getApiUrl()}/chakri/answer/${id}`, {
            method: "POST",
            body: JSON.stringify({
                answers: answers
                    }),
            headers: {
                "Content-Type": "application/json"
            },
        });

        if (!result.ok) {
            const errorResponse = await result.json();
            throw new Error(errorResponse?.error || "Cannot submit answers");
        }

    } catch (error: any) {
        throw new Error(error.message || "Cannot submit answers");
    }
}

export interface IEvaluationScore {
    score: number;
    relevanceToJd: number;
    technicalProficiency: number;
    communicationSkills: number;
    problemSolvingAbility: number;
    experience: number;
    achievement: number;
    culturalAndSoftFit: number;
}

export interface IEvaluation {
    ready: boolean;
    evaluationScore?: IEvaluationScore;
}

export async function getEvaluation(id: string): Promise<IEvaluation> {

    try {
        const result = await fetch(`${getApiUrl()}/chakri/evaluation-score/${id}`);
        if (!result.ok) {
            const errorResponse = await result.json();
            throw new Error(errorResponse?.error || "Cannot fetch evaluation");
        }

        return await result.json();

    } catch (error: any) {
        throw new Error(error.message || "Cannot fetch evaluation");
    }
}