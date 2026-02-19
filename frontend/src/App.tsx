import { useState } from "react"
import { CreateJob } from "./api/jobSchedulerBackend";

export default function App() {
  const [showCronExpressionBox, setShowCronExpressionBox] = useState(false);
  const [showFixedDelayBox, setShowFixedDelayBox] = useState(false);
  const [showEmailNotificationForm, setShowEmailNotificationForm] = useState(true);
  const [showHealthCheckForm, setShowHealthCheckForm] = useState(false);
  const [showNextExecutionInput, setShowNextExecutionInput] = useState(false);
  const [formData, setFormData] = useState({
    jobName: "",
    jobDescription: "",
    jobType: "",
    scheduleType: "",
    cronExpression: "",
    fixedDelay: 0,
    isEnabled: false,
    nextExecution: new Date().toISOString().slice(0, 16),
    jobParameters: {
      emailTo: "",
      emailFrom: "",
      emailSubject: "",
      emailBody: "",
      url: "https://"
    }
  })


  async function handleFormSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    let transformedParameters;

    if (formData.jobType === "EMAIL_NOTIFICATION") {
      transformedParameters = {
        to: formData.jobParameters.emailTo,
        from: formData.jobParameters.emailFrom,
        subject: formData.jobParameters.emailSubject,
        body: formData.jobParameters.emailBody,
      };
    } else if (formData.jobType === "HEALTH_CHECK_PING") {
      transformedParameters = {
        url: formData.jobParameters.url,
      };
    }

    const response = await CreateJob(
      formData.jobName,
      formData.jobDescription,
      formData.jobType,
      formData.scheduleType,
      formData.cronExpression,
      formData.fixedDelay,
      formData.isEnabled,
      formData.nextExecution.slice(0, 19),
      transformedParameters
    );

    console.log(response);
  }

  function handleFormChange(e: any) {
    const { name, value } = e.target;

    const jobParameterFields = ["emailTo", "emailFrom", "emailSubject", "emailBody", "url"];

    if (jobParameterFields.includes(name)) {
      setFormData(prev => ({
        ...prev,
        jobParameters: {
          ...prev.jobParameters,
          [name]: value
        }
      }));
    } else if (e.target.id === 'isEnabled') {
      setFormData(prev => ({
        ...prev,
        isEnabled: e.target.checked ? true : false
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }

    if (e.target.id === 'healthCheck' || e.target.id === 'emailNotification') {
      setShowHealthCheckForm(e.currentTarget.id === 'healthCheck');
      setShowEmailNotificationForm(e.currentTarget.id === 'emailNotification');
    }

    if (e.target.id === 'cron' || e.target.id === 'delayed' || e.target.id === 'once') {
      setShowCronExpressionBox(e.currentTarget.id === 'cron');
      setShowFixedDelayBox(e.currentTarget.id === 'delayed');
      setShowNextExecutionInput(e.currentTarget.id === 'once')
    }

    console.log(formData);
  }
  return (
    <>
      <div className="mx-auto px-8 max-w-md text-center">
        <div>
          <form className="*:my-8" onSubmit={handleFormSubmit}>
            <div className="flex flex-col">
              <label htmlFor="jobName" className="font-extrabold">Job Name</label>
              <input name="jobName" id="jobName" type="text" className="border" onChange={handleFormChange} value={formData.jobName} />
              <label htmlFor="jobDescription" className="font-extrabold">Job Description</label>
              <input name="jobDescription" id="jobDescription" type="text" className="border" onChange={handleFormChange} value={formData.jobDescription} />
            </div>
            <label htmlFor="jobType" className="font-extrabold">Job Type</label>
            <div className="flex flex-col">
              <label htmlFor="emailNotification">Email Notification</label>
              <input name="jobType" id="emailNotification" type="radio" onChange={handleFormChange} value={"EMAIL_NOTIFICATION"} />
              <label htmlFor="healthCheck">Health Check</label>
              <input name="jobType" id="healthCheck" type="radio" onChange={handleFormChange} value={"HEALTH_CHECK_PING"} />
            </div>
            <div id="emailNotificationParamsBox" className={showEmailNotificationForm ? "flex flex-col" : "hidden"}>
              <label htmlFor="emailTo">Send email(s) to</label>
              <input name="emailTo" id="emailTo" type="email" onChange={handleFormChange} value={formData.jobParameters.emailTo} />
              <label htmlFor="emailFrom">Email(s) from</label>
              <input name="emailFrom" id="emailFrom" type="email" onChange={handleFormChange} value={formData.jobParameters.emailFrom} />
              <label htmlFor="emailSubject">Subject</label>
              <input name="emailSubject" id="emailSubject" type="text" onChange={handleFormChange} value={formData.jobParameters.emailSubject} />
              <label htmlFor="emailBody">Body</label>
              <textarea name="emailBody" id="emailBody" onChange={handleFormChange} value={formData.jobParameters.emailBody} />
            </div>
            <div id="healthCheckParamsBox" className={showHealthCheckForm ? "flex flex-col" : "hidden"}>
              <label htmlFor="url">URL to monitor</label>
              <input name="url" id="url" type="text" onChange={handleFormChange} value={formData.jobParameters.url} />
            </div>
            <label htmlFor="scheduleType" className="font-extrabold">Schedule Type</label>
            <div className="flex flex-col">
              <label htmlFor="cron">CRON</label>
              <input name="scheduleType" id="cron" type="radio" onChange={handleFormChange} value={"CRON"} />
              <label htmlFor="delayed">Fixed-delay</label>
              <input name="scheduleType" id="delayed" type="radio" onChange={handleFormChange} value={"FIXED_DELAY"} />
              <label htmlFor="once">One-time</label>
              <input name="scheduleType" id="once" type="radio" onChange={handleFormChange} value={"ONE_TIME"} />
            </div>
            <div id="cronExpInputBox" className={showCronExpressionBox ? "block" : "hidden"}>
              <label htmlFor="cronExpression">CRON Expression</label>
              <input type="text" name="cronExpression" id="cronExpression" onChange={handleFormChange} value={formData.cronExpression} />
            </div>
            <div id="fixedDelayInputBox" className={showFixedDelayBox ? "block" : "hidden"}>
              <label htmlFor="fixedDelay">Delay(ms)</label>
              <input type="number" name="fixedDelay" id="fixedDelay" onChange={handleFormChange} value={formData.fixedDelay} />
            </div>
            <label htmlFor="jobSettings" className="font-extrabold">Job Settings</label>
            <div className="flex flex-col *:my-2 text-center">
              <label htmlFor="isEnabled">Job Enabled?</label>
              <input type="checkbox" name="isEnabled" id="isEnabled" onChange={handleFormChange} />
              <div className={showNextExecutionInput ? "flex flex-col *:my-2" : "hidden"}>
                <label htmlFor="nextExecution">Execution Time</label>
                <input name="nextExecution" id="nextExecution" type="datetime-local" onChange={handleFormChange} value={formData.nextExecution} />
              </div>
            </div>
            <input type="submit" value={"Create Job"} className="border p-2" />
          </form>
        </div>
      </div>
    </>
  )
}
