import { useEffect, useState } from "react"
import { CreateJob, deleteJob, getJobExecutions, getJobs, pauseJob, resumeJob, triggerJob } from "./api/jobSchedulerBackend";

export default function App() {
  const [showCronExpressionBox, setShowCronExpressionBox] = useState(false);
  const [showFixedDelayBox, setShowFixedDelayBox] = useState(false);
  const [showEmailNotificationForm, setShowEmailNotificationForm] = useState(true);
  const [showHealthCheckForm, setShowHealthCheckForm] = useState(false);
  const [showNextExecutionInput, setShowNextExecutionInput] = useState(false);
  const [jobsList, setJobsList] = useState([])
  const [executionList, setExecutionList] = useState([])
  const [formKey, setFormKey] = useState(0);
  const [formData, setFormData] = useState({
    jobName: "",
    jobDescription: "",
    jobType: "",
    scheduleType: "",
    cronExpression: "",
    fixedDelay: 5000,
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

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [jobs, executions] = await Promise.all([
          getJobs(),
          getJobExecutions()
        ]);

        setJobsList(jobs);
        setExecutionList(executions);
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    };

    fetchData();

    const interval = setInterval(fetchData, 5000);

    return () => clearInterval(interval);
  }, []);


  async function handleFormSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    setFormData({
      jobName: "",
      jobDescription: "",
      jobType: "",
      scheduleType: "",
      cronExpression: "",
      fixedDelay: 5000,
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
    setShowCronExpressionBox(false);
    setShowFixedDelayBox(false);
    setShowEmailNotificationForm(false);
    setShowHealthCheckForm(false);
    setFormKey(prev => prev + 1);
    let transformedParameters;

    if (formData.jobType === "EMAIL_NOTIFICATION") {
      transformedParameters = {
        emailTo: formData.jobParameters.emailTo,
        emailFrom: formData.jobParameters.emailFrom,
        emailSubject: formData.jobParameters.emailSubject,
        emailBody: formData.jobParameters.emailBody,
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

    console.log(response)

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

  }
  return (
    <>
      <div className="*:my-4 ">
        <h1 className="font-extrabold text-2xl text-center underline underline-offset-8">Job Scheduler Dashboard</h1>
        <div className="mx-auto grid grid-cols-2 gap-8 px-24 max-w-full ">
          <div>
            <form key={formKey} onSubmit={handleFormSubmit} className="[&>div]:my-8">
              <div className="flex flex-col">
                <label htmlFor="jobName" className="font-extrabold">Job Name</label>
                <input name="jobName" id="jobName" type="text" className="border" onChange={handleFormChange} value={formData.jobName} />
                <label htmlFor="jobDescription" className="font-extrabold">Job Description</label>
                <textarea name="jobDescription" id="jobDescription" className="border" onChange={handleFormChange} value={formData.jobDescription} />
              </div>
              <label htmlFor="jobType" className="font-extrabold">Job Type</label>
              <div className="flex *:px-4 content-center items-center">
                <label htmlFor="emailNotification">Email Notification</label>
                <input name="jobType" id="emailNotification" type="radio" onChange={handleFormChange} value={"EMAIL_NOTIFICATION"} />
                <label htmlFor="healthCheck">Health Check</label>
                <input name="jobType" id="healthCheck" type="radio" onChange={handleFormChange} value={"HEALTH_CHECK_PING"} />
              </div>
              <div id="emailNotificationParamsBox" className={showEmailNotificationForm ? "flex flex-col" : "hidden"}>
                <label htmlFor="emailTo">Send email(s) to</label>
                <input name="emailTo" id="emailTo" type="email" onChange={handleFormChange} value={formData.jobParameters.emailTo} className="border" />
                <label htmlFor="emailFrom">Email(s) from</label>
                <input name="emailFrom" id="emailFrom" type="email" onChange={handleFormChange} value={formData.jobParameters.emailFrom} className="border" />
                <label htmlFor="emailSubject">Subject</label>
                <input name="emailSubject" id="emailSubject" type="text" onChange={handleFormChange} value={formData.jobParameters.emailSubject} className="border" />
                <label htmlFor="emailBody">Body</label>
                <textarea name="emailBody" id="emailBody" onChange={handleFormChange} value={formData.jobParameters.emailBody} className="border" />
              </div>
              <div id="healthCheckParamsBox" className={showHealthCheckForm ? "block *:px-4" : "hidden"}>
                <label htmlFor="url">URL to monitor</label>
                <input name="url" id="url" type="text" onChange={handleFormChange} value={formData.jobParameters.url} className="border" />
              </div>
              <label htmlFor="scheduleType" className="font-extrabold">Schedule Type</label>
              <div className="flex *:px-4 content-center items-center">
                <label htmlFor="cron">CRON</label>
                <input name="scheduleType" id="cron" type="radio" onChange={handleFormChange} value={"CRON"} />
                <label htmlFor="delayed">Fixed-delay</label>
                <input name="scheduleType" id="delayed" type="radio" onChange={handleFormChange} value={"FIXED_DELAY"} />
                <label htmlFor="once">One-time</label>
                <input name="scheduleType" id="once" type="radio" onChange={handleFormChange} value={"ONE_TIME"} />
              </div>
              <div id="cronExpInputBox" className={showCronExpressionBox ? "block *:px-4" : "hidden"}>
                <label htmlFor="cronExpression">CRON Expression</label>
                <input type="text" name="cronExpression" id="cronExpression" onChange={handleFormChange} value={formData.cronExpression} className="border mb-8" />
                <div>
                  <div className="[&>p]:tracking-tighter">
                    <small className="text-gray-600">
                      Format: second minute hour day month dayOfWeek
                      <br />
                      Example: "0 */5 * * * *" (every 5 minutes)
                    </small>                  </div>
                  <p className="tracking-widest">* * * * * *</p>
                </div>
              </div>
              <div id="fixedDelayInputBox" className={showFixedDelayBox ? "block *:px-4" : "hidden"}>
                <label htmlFor="fixedDelay">Delay(ms)</label>
                <input type="number" name="fixedDelay" id="fixedDelay" onChange={handleFormChange} value={formData.fixedDelay} className="border" min={5000} />
              </div>
              <label htmlFor="jobSettings" className="font-extrabold">Job Settings</label>
              <div className="flex items-center content-center *:px-2">
                <label htmlFor="isEnabled">Job Enabled?</label>
                <input type="checkbox" name="isEnabled" id="isEnabled" onChange={handleFormChange} />
                <div className={showNextExecutionInput ? "flex *:px-4 content-center items-center" : "hidden"}>
                  <label htmlFor="nextExecution">Execution Time</label>
                  <input name="nextExecution" id="nextExecution" type="datetime-local" onChange={handleFormChange} value={formData.nextExecution} className="border" />
                </div>
              </div>
              <input type="submit" value={"Create Job"} className="border p-2 w-full hover:bg-gray-800/20" />
            </form>
          </div>
          <div className="grid grid-cols-1 gap-16 mt-8">
            <div className="overflow-y-scroll max-h-100">
              <h2 className="font-extrabold">Jobs</h2>
              <div className="*:my-2">
                {jobsList?.map(job => {
                  return (
                    <div key={job.id} className="[&>p]:my-4 border max-w-full px-4">
                      <p className="font-semibold">{job.jobName}</p>
                      <p>Description: {job.jobDescription}</p>
                      <p>Enabled: {job.isEnabled ? "✓" : "✕"}</p>
                      <p>Job Status: {job.jobStatus}</p>
                      <p>Next Execution: {new Date(job.nextExecution).toLocaleString()}</p>
                      <div className="my-2 [&>button]:px-8 [&>button]:mr-8">
                        {job.isEnabled ?
                          <button className="border p-2 hover:bg-gray-800/50" onClick={() => pauseJob(job.id)}>Pause</button>
                          :
                          <button className="border p-2 hover:bg-gray-800/50" onClick={() => resumeJob(job.id)}>Resume</button>
                        }
                        <button className="border p-2 hover:bg-gray-800/50" onClick={() => triggerJob(job.id)}>Trigger Manually</button>
                        <button className="border p-2 hover:bg-red-800/50" onClick={() => deleteJob(job.id)}>Delete</button>
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
            <div>
              <h2 className="font-extrabold">Job Executions</h2>
              <div className="*:my-2 ">
                {executionList?.map(execution => {
                  return (
                    <div key={execution.id} className="border max-w-full px-4">
                      {execution.message ?
                        <p className="flex justify-between">{execution.jobName} <span className={execution.message?.includes("Passed!") || execution.message?.includes("successfully") ? "text-green-500 font-extrabold" : "text-red-500 font-extrabold"}> {execution.message}</span></p>
                        :
                        <p className="flex justify-between">{execution.jobName} <span className={execution.message?.includes("Passed!") || execution.message?.includes("successfully") ? "text-green-500 font-extrabold" : "text-red-500 font-extrabold"}> {execution.errorMessage}</span></p>
                      }
                    </div>
                  )
                })}
              </div>
            </div>
          </div>
        </div >
        <div className="border-t-2">
          STATUS 3
        </div>
      </div>
    </>
  )
}
