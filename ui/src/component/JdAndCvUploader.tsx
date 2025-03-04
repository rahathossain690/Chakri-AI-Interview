import React, { JSX, useState } from 'react';
import { submitJdAndCv } from '../service/apiService';
import { showError } from '../service/dialogService';
import '../css/JdAndCvUploader.css';

function JdAndCvUploader({moveToQnA}: {moveToQnA: (id: string) => void}): JSX.Element {

    const [jobDescriptionFile, setJobDescriptionFile] = useState<File | null>(null);
    const [cvFile, setCvFile] = useState<File | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
  
    const handleJobDescriptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      if (event.target.files) {
        setJobDescriptionFile(event.target.files[0]);

      } else {
        setCvFile(null);
      }
    };
  
    const handleCvChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      if (event.target.files) {
        setCvFile(event.target.files[0]);

      } else {
        setCvFile(null);
      }
    };
  
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      setIsSubmitting(true);
  
      if (!jobDescriptionFile || !cvFile) {
        showError('Please select both files');
        setIsSubmitting(false);
        return;
      }

      if (!jobDescriptionFile.name.endsWith('.pdf') || !cvFile.name.endsWith('.pdf')) {
        showError('Both files must be PDFs');
        setIsSubmitting(false);
        return;
      }

      submitJdAndCv(jobDescriptionFile, cvFile)
      .then((id) => {
        moveToQnA(id);
      })
      .catch((error) => {
        showError(error?.message || "Cannot submit!");
        setIsSubmitting(false);
      });
    };
  
    return (
      <form onSubmit={handleSubmit} className="file-upload-form">
        <div className="input-group">
          <label htmlFor="jobDescription" className="input-label">
            Job Description:
          </label>
          <input
            type="file"
            id="jobDescription"
            onChange={handleJobDescriptionChange}
            className="file-input"
          />
        </div>
        <div className="input-group">
          <label htmlFor="cv" className="input-label">
            CV:
          </label>
          <input type="file" id="cv" onChange={handleCvChange} className="file-input" />
        </div>
        <button type="submit" disabled={isSubmitting} className="submit-button">
          {isSubmitting ? 'Submitting...' : 'Submit'}
        </button>
      </form>
    );
  }

export default JdAndCvUploader;