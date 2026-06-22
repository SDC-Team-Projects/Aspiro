import axios from "axios";
import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  createTemplate,
  createTemplateStage,
  createTemplateTask,
  deleteTemplate,
  deleteTemplateStage,
  deleteTemplateTask,
  getTemplateById,
  getTemplates,
  updateTemplate,
  updateTemplateStage,
  updateTemplateTask,
} from "../api/templateApi";
import type { Template, TemplateStage, TemplateTask } from "../types/template";

export default function AdminPage() {
  const [templates, setTemplates] = useState<Template[]>([]);
  const [selectedTemplate, setSelectedTemplate] = useState<Template | null>(
    null
  );
  const [selectedStageId, setSelectedStageId] = useState<number | null>(null);

  const [templateTitle, setTemplateTitle] = useState("");
  const [templateDescription, setTemplateDescription] = useState("");

  const [stageTitle, setStageTitle] = useState("");
  const [stageOrderNumber, setStageOrderNumber] = useState(1);

  const [taskTitle, setTaskTitle] = useState("");
  const [taskDescription, setTaskDescription] = useState("");
  const [taskOrderNumber, setTaskOrderNumber] = useState(1);
  const [taskDaysOffset, setTaskDaysOffset] = useState(0);
  const [taskDurationDays, setTaskDurationDays] = useState(1);

  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const stages: TemplateStage[] = selectedTemplate?.stages || [];

  async function loadTemplates() {
    try {
      setError("");
      const data = await getTemplates();
      setTemplates(data);
    } catch (error) {
      console.error("Failed to load templates:", error);
      setError("Failed to load templates.");
    } finally {
      setLoading(false);
    }
  }

  async function reloadSelectedTemplate(templateId: number) {
    const updatedTemplate = await getTemplateById(templateId);
    setSelectedTemplate(updatedTemplate);
  }

  useEffect(() => {
    loadTemplates();
  }, []);

  async function handleSelectTemplate(templateId: number) {
    setError("");
    setMessage("");
    setSelectedStageId(null);

    try {
      const data = await getTemplateById(templateId);
      setSelectedTemplate(data);
    } catch (error) {
      console.error("Failed to load selected template:", error);
      setError("Failed to load selected template.");
    }
  }

  async function handleCreateTemplate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError("");
    setMessage("");
    setActionLoading(true);

    try {
      const createdTemplate = await createTemplate({
        title: templateTitle,
        description: templateDescription,
      });

      setTemplateTitle("");
      setTemplateDescription("");

      await loadTemplates();
      await reloadSelectedTemplate(createdTemplate.id);

      setMessage("Template created successfully.");
    } catch (error) {
      console.error("Failed to create template:", error);

      if (axios.isAxiosError(error)) {
        setError(
          `Failed to create template. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to create template.");
      }
    } finally {
      setActionLoading(false);
    }
  }

  async function handleCreateStage(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!selectedTemplate) {
      setError("Select template first.");
      return;
    }

    setError("");
    setMessage("");
    setActionLoading(true);

    try {
      await createTemplateStage(selectedTemplate.id, {
        title: stageTitle,
        orderNumber: stageOrderNumber,
      });

      const updatedTemplate = await getTemplateById(selectedTemplate.id);
      setSelectedTemplate(updatedTemplate);

      const updatedStages = updatedTemplate.stages || [];

      if (updatedStages.length > 0) {
        const lastStage = updatedStages[updatedStages.length - 1];
        setSelectedStageId(lastStage.id);
      }

      setStageTitle("");
      setStageOrderNumber(stageOrderNumber + 1);

      await loadTemplates();

      setMessage("Stage created successfully and selected automatically.");
    } catch (error) {
      console.error("Failed to create stage:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);

        setError(
          `Failed to create stage. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to create stage.");
      }
    } finally {
      setActionLoading(false);
    }
  }

  async function handleCreateTask(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!selectedTemplate) {
      setError("Select template first.");
      return;
    }

    if (!selectedStageId) {
      setError("Select stage first.");
      return;
    }

    setError("");
    setMessage("");
    setActionLoading(true);

    try {
      await createTemplateTask(selectedStageId, {
        title: taskTitle,
        description: taskDescription,
        orderNumber: taskOrderNumber,
        daysOffset: taskDaysOffset,
        durationDays: taskDurationDays,
      });

      setTaskTitle("");
      setTaskDescription("");
      setTaskOrderNumber(taskOrderNumber + 1);
      setTaskDaysOffset(taskDaysOffset + taskDurationDays);
      setTaskDurationDays(1);

      await reloadSelectedTemplate(selectedTemplate.id);
      await loadTemplates();

      setMessage("Task created successfully.");
    } catch (error) {
      console.error("Failed to create task:", error);

      if (axios.isAxiosError(error)) {
        setError(
          `Failed to create task. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to create task.");
      }
    } finally {
      setActionLoading(false);
    }
  }

  async function handleDeleteTemplate(templateId: number) {
  const confirmed = window.confirm(
    "Delete this template? This action cannot be undone."
  );

  if (!confirmed) {
    return;
  }

  setError("");
  setMessage("");
  setActionLoading(true);

  try {
    await deleteTemplate(templateId);

    if (selectedTemplate?.id === templateId) {
      setSelectedTemplate(null);
      setSelectedStageId(null);
    }

    await loadTemplates();

    setMessage("Template deleted successfully.");
  } catch (error) {
    console.error("Failed to delete template:", error);

    if (axios.isAxiosError(error)) {
      console.error("Status:", error.response?.status);
      console.error("Response:", error.response?.data);

      if (error.response?.status === 400) {
        setError(
          "This template is already used in existing goals and cannot be deleted."
        );
      } else {
        setError("Failed to delete template. Please try again later.");
      }
    } else {
      setError("Failed to delete template.");
    }
  } finally {
    setActionLoading(false);
  }
}

  async function handleDeleteStage(stageId: number) {
  const confirmed = window.confirm(
    "Delete this stage? Tasks inside this stage should be deleted first."
  );

  if (!confirmed || !selectedTemplate) {
    return;
  }

  setError("");
  setMessage("");
  setActionLoading(true);

  try {
    await deleteTemplateStage(stageId);

    const updatedTemplate = await getTemplateById(selectedTemplate.id);
    setSelectedTemplate(updatedTemplate);

    if (selectedStageId === stageId) {
      setSelectedStageId(null);
    }

    await loadTemplates();

    setMessage("Stage deleted successfully.");
  } catch (error) {
    console.error("Failed to delete stage:", error);

    if (axios.isAxiosError(error)) {
      console.error("Status:", error.response?.status);
      console.error("Response:", error.response?.data);

      if (error.response?.status === 400) {
        setError(
          "This stage contains tasks or is already used in existing goals and cannot be deleted."
        );
      } else {
        setError("Failed to delete stage. Please try again later.");
      }
    } else {
      setError("Failed to delete stage.");
    }
  } finally {
    setActionLoading(false);
  }
}

  async function handleDeleteTask(taskId: number) {
    const confirmed = window.confirm("Delete this task?");

    if (!confirmed || !selectedTemplate) {
      return;
    }

    setError("");
    setMessage("");
    setActionLoading(true);

    try {
      await deleteTemplateTask(taskId);

      const updatedTemplate = await getTemplateById(selectedTemplate.id);
      setSelectedTemplate(updatedTemplate);

      await loadTemplates();

      setMessage("Task deleted successfully.");
    } catch (error) {
      console.error("Failed to delete task:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);
      if (error.response?.status === 400) {
        setError(
          "This task is already used in an existing goal and cannot be deleted."
        );
      } else {
        setError("Failed to delete task. Please try again later.");
      }
    }else {
      setError("Failed to delete task.");
    }
    } finally {
      setActionLoading(false);
    }
  }

async function handleEditTemplate(template: Template) {
  const newTitle = window.prompt("Template title:", template.title);

  if (newTitle === null) {
    return;
  }

  const newDescription = window.prompt(
    "Template description:",
    template.description || ""
  );

  if (newDescription === null) {
    return;
  }

  if (!newTitle.trim()) {
    setError("Template title cannot be empty.");
    return;
  }

  setError("");
  setMessage("");
  setActionLoading(true);

  try {
    await updateTemplate(template.id, {
      title: newTitle.trim(),
      description: newDescription.trim(),
    });

    await loadTemplates();

    if (selectedTemplate?.id === template.id) {
      await reloadSelectedTemplate(template.id);
    }

    setMessage("Template updated successfully.");
  } catch (error) {
    console.error("Failed to update template:", error);

    if (axios.isAxiosError(error)) {
      console.error("Status:", error.response?.status);
      console.error("Response:", error.response?.data);
      setError("Failed to update template. Please try again later.");
    } else {
      setError("Failed to update template.");
    }
  } finally {
    setActionLoading(false);
  }
}

async function handleEditStage(stage: TemplateStage) {
  if (!selectedTemplate) {
    setError("Select template first.");
    return;
  }

  const newTitle = window.prompt("Stage title:", stage.title);

  if (newTitle === null) {
    return;
  }

  const newOrderNumberRaw = window.prompt(
    "Stage order number:",
    String(stage.orderNumber || 1)
  );

  if (newOrderNumberRaw === null) {
    return;
  }

  const newOrderNumber = Number(newOrderNumberRaw);

  if (!newTitle.trim()) {
    setError("Stage title cannot be empty.");
    return;
  }

  if (!Number.isFinite(newOrderNumber) || newOrderNumber < 1) {
    setError("Stage order number must be 1 or greater.");
    return;
  }

  setError("");
  setMessage("");
  setActionLoading(true);

  try {
    await updateTemplateStage(stage.id, {
      title: newTitle.trim(),
      orderNumber: newOrderNumber,
    });

    await reloadSelectedTemplate(selectedTemplate.id);
    await loadTemplates();

    setMessage("Stage updated successfully.");
  } catch (error) {
    console.error("Failed to update stage:", error);

    if (axios.isAxiosError(error)) {
      console.error("Status:", error.response?.status);
      console.error("Response:", error.response?.data);
      setError("Failed to update stage. Please try again later.");
    } else {
      setError("Failed to update stage.");
    }
  } finally {
    setActionLoading(false);
  }
}

async function handleEditTask(task: TemplateTask) {
  if (!selectedTemplate) {
    setError("Select template first.");
    return;
  }

  const newTitle = window.prompt("Task title:", task.title);

  if (newTitle === null) {
    return;
  }

  const newDescription = window.prompt(
    "Task description:",
    task.description || ""
  );

  if (newDescription === null) {
    return;
  }

  const newOrderNumberRaw = window.prompt(
    "Task order number:",
    String(task.orderNumber || 1)
  );

  if (newOrderNumberRaw === null) {
    return;
  }

  const newDaysOffsetRaw = window.prompt(
    "Days offset:",
    String(task.daysOffset || 0)
  );

  if (newDaysOffsetRaw === null) {
    return;
  }

  const newDurationDaysRaw = window.prompt(
    "Duration days:",
    String(task.durationDays || 1)
  );

  if (newDurationDaysRaw === null) {
    return;
  }

  const newOrderNumber = Number(newOrderNumberRaw);
  const newDaysOffset = Number(newDaysOffsetRaw);
  const newDurationDays = Number(newDurationDaysRaw);

  if (!newTitle.trim()) {
    setError("Task title cannot be empty.");
    return;
  }

  if (!Number.isFinite(newOrderNumber) || newOrderNumber < 1) {
    setError("Task order number must be 1 or greater.");
    return;
  }

  if (!Number.isFinite(newDaysOffset) || newDaysOffset < 0) {
    setError("Days offset must be 0 or greater.");
    return;
  }

  if (!Number.isFinite(newDurationDays) || newDurationDays < 1) {
    setError("Duration days must be 1 or greater.");
    return;
  }

  setError("");
  setMessage("");
  setActionLoading(true);

  try {
    await updateTemplateTask(task.id, {
      title: newTitle.trim(),
      description: newDescription.trim(),
      orderNumber: newOrderNumber,
      daysOffset: newDaysOffset,
      durationDays: newDurationDays,
    });

    await reloadSelectedTemplate(selectedTemplate.id);
    await loadTemplates();

    setMessage("Task updated successfully.");
  } catch (error) {
    console.error("Failed to update task:", error);

    if (axios.isAxiosError(error)) {
      console.error("Status:", error.response?.status);
      console.error("Response:", error.response?.data);
      setError("Failed to update task. Please try again later.");
    } else {
      setError("Failed to update task.");
    }
  } finally {
    setActionLoading(false);
  }
}

  if (loading) {
  return <p>Loading admin panel...</p>;
}

return (
  <section>
    <h1>Admin Panel</h1>

    {error && <p className="error">{error}</p>}
    {message && <p className="success">{message}</p>}

    <div className="admin-layout">
      <div className="admin-column">
        <div className="card">
          <h2>Create template</h2>

          <form className="form" onSubmit={handleCreateTemplate}>
            <label>
              Title
              <input
                type="text"
                value={templateTitle}
                onChange={(event) => setTemplateTitle(event.target.value)}
                placeholder="Java Developer Path"
                required
              />
            </label>

            <label>
              Description
              <input
                type="text"
                value={templateDescription}
                onChange={(event) =>
                  setTemplateDescription(event.target.value)
                }
                placeholder="Step-by-step learning path"
              />
            </label>

            <button type="submit" disabled={actionLoading}>
              {actionLoading ? "Creating..." : "Create template"}
            </button>
          </form>
        </div>

        <div className="card">
          <h2>Templates</h2>

          {templates.length === 0 && <p>No templates found.</p>}

          <div className="admin-list">
            {templates.map((template) => (
              <div key={template.id} className="admin-list-row">
                <button
                  type="button"
                  className={
                    selectedTemplate?.id === template.id
                      ? "list-button selected"
                      : "list-button"
                  }
                  onClick={() => handleSelectTemplate(template.id)}
                >
                  {template.title}
                </button>

                <button
                  type="button"
                  className="secondary-small-button"
                  disabled={actionLoading}
                  onClick={() => handleEditTemplate(template)}
                >
                  Edit
                </button>

                <button
                  type="button"
                  className="danger-small-button"
                  disabled={actionLoading}
                  onClick={() => handleDeleteTemplate(template.id)}
                >
                  Delete
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="admin-column">
        <div className="card">
          <h2>Selected template</h2>

          {!selectedTemplate ? (
            <p>Select a template from the list.</p>
          ) : (
            <>
              <h3>{selectedTemplate.title}</h3>
              <p>{selectedTemplate.description || "No description"}</p>

              <h3>Stages</h3>

              {stages.length === 0 && <p>No stages yet.</p>}

              <div className="admin-list">
                {stages.map((stage) => (
                  <button
                    key={stage.id}
                    type="button"
                    className={
                      selectedStageId === stage.id
                        ? "list-button selected"
                        : "list-button"
                    }
                    onClick={() => setSelectedStageId(stage.id)}
                  >
                    {stage.orderNumber}. {stage.title}
                  </button>
                ))}
              </div>
            </>
          )}
        </div>

        <div className="card">
          <h2>Add stage</h2>

          <form className="form" onSubmit={handleCreateStage}>
            <label>
              Stage title
              <input
                type="text"
                value={stageTitle}
                onChange={(event) => setStageTitle(event.target.value)}
                placeholder="Java Basics"
                required
              />
            </label>

            <label>
              Order number
              <input
                type="number"
                value={stageOrderNumber}
                onChange={(event) =>
                  setStageOrderNumber(Number(event.target.value))
                }
                min={1}
                required
              />
            </label>

            <button
              type="submit"
              disabled={actionLoading || !selectedTemplate}
            >
              {actionLoading ? "Adding..." : "Add stage"}
            </button>
          </form>
        </div>
      </div>

      <div className="admin-column">
        <div className="card">
          <h2>Add task</h2>

          {!selectedTemplate && <p>Select template first.</p>}
          {selectedTemplate && stages.length === 0 && (
            <p>Create stage first.</p>
          )}

          <form className="form" onSubmit={handleCreateTask}>
            <label>
              Stage
              <select
                value={selectedStageId ?? ""}
                onChange={(event) =>
                  setSelectedStageId(Number(event.target.value))
                }
                required
              >
                <option value="">Select stage</option>

                {stages.map((stage) => (
                  <option key={stage.id} value={stage.id}>
                    {stage.orderNumber}. {stage.title}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Task title
              <input
                type="text"
                value={taskTitle}
                onChange={(event) => setTaskTitle(event.target.value)}
                placeholder="Learn Java variables"
                required
              />
            </label>

            <label>
              Description
              <input
                type="text"
                value={taskDescription}
                onChange={(event) => setTaskDescription(event.target.value)}
                placeholder="Learn primitive types and syntax"
              />
            </label>

            <label>
              Order number
              <input
                type="number"
                value={taskOrderNumber}
                onChange={(event) =>
                  setTaskOrderNumber(Number(event.target.value))
                }
                min={1}
                required
              />
            </label>

            <label>
              Days offset
              <input
                type="number"
                value={taskDaysOffset}
                onChange={(event) =>
                  setTaskDaysOffset(Number(event.target.value))
                }
                min={0}
                required
              />
            </label>

            <label>
              Duration days
              <input
                type="number"
                value={taskDurationDays}
                onChange={(event) =>
                  setTaskDurationDays(Number(event.target.value))
                }
                min={1}
                required
              />
            </label>

            <button
              type="submit"
              disabled={actionLoading || !selectedTemplate || !selectedStageId}
            >
              {actionLoading ? "Adding..." : "Add task"}
            </button>
          </form>
        </div>

        <div className="card">
          <h2>Preview</h2>

          {!selectedTemplate ? (
            <p>No template selected.</p>
          ) : stages.length === 0 ? (
            <p>No stages in this template.</p>
          ) : (
            <div className="stages-list">
              {stages.map((stage) => (
                <article key={stage.id} className="stage-card">
                  <div className="stage-header">
                    <div>
                      <h3>
                        {stage.orderNumber}. {stage.title}
                      </h3>
                    </div>

                    <div className="row-actions">
                      <button
                        type="button"
                        className="secondary-small-button"
                        disabled={actionLoading}
                        onClick={() => handleEditStage(stage)}
                      >
                        Edit stage
                      </button>

                      <button
                        type="button"
                        className="danger-small-button"
                        disabled={actionLoading}
                        onClick={() => handleDeleteStage(stage.id)}
                      >
                        Delete stage
                      </button>
                    </div>
                  </div>

                  {!stage.tasks || stage.tasks.length === 0 ? (
                    <p>No tasks in this stage.</p>
                  ) : (
                    <div className="task-list">
                      {stage.tasks.map((task) => (
                        <div key={task.id} className="task-row">
                          <div>
                            <strong>
                              {task.orderNumber}. {task.title}
                            </strong>

                            {task.description && <p>{task.description}</p>}

                            <p>
                              Offset: {task.daysOffset}, duration:{" "}
                              {task.durationDays}
                            </p>
                          </div>

                          <div className="row-actions">
                            <button
                              type="button"
                              className="secondary-small-button"
                              disabled={actionLoading}
                              onClick={() => handleEditTask(task)}
                            >
                              Edit task
                            </button>

                            <button
                              type="button"
                              className="danger-small-button"
                              disabled={actionLoading}
                              onClick={() => handleDeleteTask(task.id)}
                            >
                              Delete task
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </article>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  </section>
);
}