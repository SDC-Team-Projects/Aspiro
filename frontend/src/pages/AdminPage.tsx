import axios from "axios";
import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  archiveTemplate,
  createTemplate,
  createTemplateStage,
  createTemplateTask,
  deleteTemplate,
  deleteTemplateStage,
  deleteTemplateTask,
  getAdminTemplates,
  getTemplateById,
  restoreTemplate,
  updateTemplate,
  updateTemplateStage,
  updateTemplateTask,
} from "../api/templateApi";
import ConfirmModal from "../components/ConfirmModal";
import EditModal from "../components/EditModal";
import type { Template, TemplateStage, TemplateTask } from "../types/template";

type ConfirmAction = () => void | Promise<void>;
type EditMode = "template" | "stage" | "task";
type TemplateFilter = "ALL" | "ACTIVE" | "ARCHIVED";

export default function AdminPage() {
  const [templates, setTemplates] = useState<Template[]>([]);
  const [selectedTemplate, setSelectedTemplate] = useState<Template | null>(
    null
  );
  const [selectedStageId, setSelectedStageId] = useState<number | null>(null);

  const [templateTitle, setTemplateTitle] = useState("");
  const [templateDescription, setTemplateDescription] = useState("");
  const [templateCoverImageUrl, setTemplateCoverImageUrl] = useState("");

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

  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [confirmTitle, setConfirmTitle] = useState("");
  const [confirmMessage, setConfirmMessage] = useState("");
  const [confirmText, setConfirmText] = useState("Confirm");
  const [confirmDanger, setConfirmDanger] = useState(false);
  const [confirmAction, setConfirmAction] = useState<ConfirmAction | null>(
    null
  );

  const [editMode, setEditMode] = useState<EditMode | null>(null);
  const [editingTemplate, setEditingTemplate] = useState<Template | null>(null);
  const [editingStage, setEditingStage] = useState<TemplateStage | null>(null);
  const [editingTask, setEditingTask] = useState<TemplateTask | null>(null);

  const [editTitle, setEditTitle] = useState("");
  const [editDescription, setEditDescription] = useState("");
  const [editOrderNumber, setEditOrderNumber] = useState(1);
  const [editDaysOffset, setEditDaysOffset] = useState(0);
  const [editDurationDays, setEditDurationDays] = useState(1);
  const [editCoverImageUrl, setEditCoverImageUrl] = useState("");

  const stages: TemplateStage[] = selectedTemplate?.stages || [];
  const [templateFilter, setTemplateFilter] = useState<TemplateFilter>("ALL");

  const filteredTemplates = templates.filter((template) => {
    if (templateFilter === "ACTIVE") {
      return !template.archived;
    }

    if (templateFilter === "ARCHIVED") {
      return template.archived;
    }

    return true;
  });

  const isEditModalOpen = editMode !== null;

  async function loadTemplates() {
    try {
      setError("");
      const data = await getAdminTemplates();
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

  function openConfirmModal(
    title: string,
    message: string,
    action: ConfirmAction,
    options?: {
      confirmText?: string;
      danger?: boolean;
    }
  ) {
    setConfirmTitle(title);
    setConfirmMessage(message);
    setConfirmText(options?.confirmText || "Confirm");
    setConfirmDanger(options?.danger || false);
    setConfirmAction(() => action);
    setConfirmModalOpen(true);
  }

  function closeConfirmModal() {
    setConfirmModalOpen(false);
    setConfirmTitle("");
    setConfirmMessage("");
    setConfirmText("Confirm");
    setConfirmDanger(false);
    setConfirmAction(null);
  }

  async function handleConfirmAction() {
    if (!confirmAction) {
      return;
    }

    await confirmAction();
    closeConfirmModal();
  }

  function closeEditModal() {
    setEditMode(null);
    setEditingTemplate(null);
    setEditingStage(null);
    setEditingTask(null);
    setEditTitle("");
    setEditDescription("");
    setEditOrderNumber(1);
    setEditDaysOffset(0);
    setEditDurationDays(1);
    setEditCoverImageUrl("");
  }

  function getEditModalTitle() {
    if (editMode === "template") {
      return "Edit template";
    }

    if (editMode === "stage") {
      return "Edit stage";
    }

    if (editMode === "task") {
      return "Edit task";
    }

    return "Edit";
  }

  function getEditSubmitText() {
    if (editMode === "template") {
      return "Save template";
    }

    if (editMode === "stage") {
      return "Save stage";
    }

    if (editMode === "task") {
      return "Save task";
    }

    return "Save changes";
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
        coverImageUrl: templateCoverImageUrl,
      });

      setTemplateTitle("");
      setTemplateDescription("");
      setTemplateCoverImageUrl("");

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

  function handleDeleteTemplate(templateId: number) {
    openConfirmModal(
      "Delete template",
      "Are you sure you want to delete this template? This action cannot be undone.",
      async () => {
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
                "This template is already used in existing goals and cannot be deleted. Archive it instead."
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
      },
      {
        confirmText: "Delete",
        danger: true,
      }
    );
  }

  function handleArchiveTemplate(templateId: number) {
    openConfirmModal(
      "Archive template",
      "This template will be hidden from regular users, but existing goals will remain safe.",
      async () => {
        setError("");
        setMessage("");
        setActionLoading(true);

        try {
          await archiveTemplate(templateId);

          await loadTemplates();

          if (selectedTemplate?.id === templateId) {
            await reloadSelectedTemplate(templateId);
          }

          setMessage("Template archived successfully.");
        } catch (error) {
          console.error("Failed to archive template:", error);

          if (axios.isAxiosError(error)) {
            console.error("Status:", error.response?.status);
            console.error("Response:", error.response?.data);
            setError("Failed to archive template. Please try again later.");
          } else {
            setError("Failed to archive template.");
          }
        } finally {
          setActionLoading(false);
        }
      },
      {
        confirmText: "Archive",
        danger: false,
      }
    );
  }

  function handleRestoreTemplate(templateId: number) {
    openConfirmModal(
      "Restore template",
      "This template will become visible to regular users again.",
      async () => {
        setError("");
        setMessage("");
        setActionLoading(true);

        try {
          await restoreTemplate(templateId);

          await loadTemplates();

          if (selectedTemplate?.id === templateId) {
            await reloadSelectedTemplate(templateId);
          }

          setMessage("Template restored successfully.");
        } catch (error) {
          console.error("Failed to restore template:", error);

          if (axios.isAxiosError(error)) {
            console.error("Status:", error.response?.status);
            console.error("Response:", error.response?.data);
            setError("Failed to restore template. Please try again later.");
          } else {
            setError("Failed to restore template.");
          }
        } finally {
          setActionLoading(false);
        }
      },
      {
        confirmText: "Restore",
        danger: false,
      }
    );
  }

  function handleDeleteStage(stageId: number) {
    if (!selectedTemplate) {
      setError("Select template first.");
      return;
    }

    openConfirmModal(
      "Delete stage",
      "Are you sure you want to delete this stage? Delete all tasks inside it first.",
      async () => {
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
      },
      {
        confirmText: "Delete",
        danger: true,
      }
    );
  }

  function handleDeleteTask(taskId: number) {
    if (!selectedTemplate) {
      setError("Select template first.");
      return;
    }

    openConfirmModal(
      "Delete task",
      "Are you sure you want to delete this task?",
      async () => {
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
          } else {
            setError("Failed to delete task.");
          }
        } finally {
          setActionLoading(false);
        }
      },
      {
        confirmText: "Delete",
        danger: true,
      }
    );
  }

  function handleEditTemplate(template: Template) {
    setError("");
    setMessage("");
    setEditMode("template");
    setEditingTemplate(template);
    setEditingStage(null);
    setEditingTask(null);
    setEditTitle(template.title);
    setEditDescription(template.description || "");
    setEditCoverImageUrl(template.coverImageUrl || "");
    setEditOrderNumber(1);
    setEditDaysOffset(0);
    setEditDurationDays(1);
  }

  function handleEditStage(stage: TemplateStage) {
    setError("");
    setMessage("");
    setEditMode("stage");
    setEditingStage(stage);
    setEditingTemplate(null);
    setEditingTask(null);
    setEditTitle(stage.title);
    setEditDescription("");
    setEditCoverImageUrl("");
    setEditOrderNumber(stage.orderNumber || 1);
    setEditDaysOffset(0);
    setEditDurationDays(1);
  }

  function handleEditTask(task: TemplateTask) {
    setError("");
    setMessage("");
    setEditMode("task");
    setEditingTask(task);
    setEditingTemplate(null);
    setEditingStage(null);
    setEditTitle(task.title);
    setEditDescription(task.description || "");
    setEditCoverImageUrl("");
    setEditOrderNumber(task.orderNumber || 1);
    setEditDaysOffset(task.daysOffset || 0);
    setEditDurationDays(task.durationDays || 1);
  }

  async function handleEditSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!editMode) {
      return;
    }

    if (!editTitle.trim()) {
      setError("Title cannot be empty.");
      return;
    }

    if (
      (editMode === "stage" || editMode === "task") &&
      (!Number.isFinite(editOrderNumber) || editOrderNumber < 1)
    ) {
      setError("Order number must be 1 or greater.");
      return;
    }

    if (
      editMode === "task" &&
      (!Number.isFinite(editDaysOffset) || editDaysOffset < 0)
    ) {
      setError("Days offset must be 0 or greater.");
      return;
    }

    if (
      editMode === "task" &&
      (!Number.isFinite(editDurationDays) || editDurationDays < 1)
    ) {
      setError("Duration days must be 1 or greater.");
      return;
    }

    setError("");
    setMessage("");
    setActionLoading(true);

    try {
      if (editMode === "template") {
        if (!editingTemplate) {
          setError("Template was not selected for editing.");
          return;
        }

        await updateTemplate(editingTemplate.id, {
          title: editTitle.trim(),
          description: editDescription.trim(),
          coverImageUrl: editCoverImageUrl.trim(),
        });

        await loadTemplates();

        if (selectedTemplate?.id === editingTemplate.id) {
          await reloadSelectedTemplate(editingTemplate.id);
        }

        setMessage("Template updated successfully.");
      }

      if (editMode === "stage") {
        if (!editingStage || !selectedTemplate) {
          setError("Stage was not selected for editing.");
          return;
        }

        await updateTemplateStage(editingStage.id, {
          title: editTitle.trim(),
          orderNumber: editOrderNumber,
        });

        await reloadSelectedTemplate(selectedTemplate.id);
        await loadTemplates();

        setMessage("Stage updated successfully.");
      }

      if (editMode === "task") {
        if (!editingTask || !selectedTemplate) {
          setError("Task was not selected for editing.");
          return;
        }

        await updateTemplateTask(editingTask.id, {
          title: editTitle.trim(),
          description: editDescription.trim(),
          orderNumber: editOrderNumber,
          daysOffset: editDaysOffset,
          durationDays: editDurationDays,
        });

        await reloadSelectedTemplate(selectedTemplate.id);
        await loadTemplates();

        setMessage("Task updated successfully.");
      }

      closeEditModal();
    } catch (error) {
      console.error("Failed to update item:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);
        setError("Failed to update item. Please try again later.");
      } else {
        setError("Failed to update item.");
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

              <label>
                Cover image URL
                <input
                  type="text"
                  value={templateCoverImageUrl}
                  onChange={(event) =>
                    setTemplateCoverImageUrl(event.target.value)
                  }
                  placeholder="https://example.com/image.jpg"
                />
              </label>

              <button type="submit" disabled={actionLoading}>
                {actionLoading ? "Creating..." : "Create template"}
              </button>
            </form>
          </div>

          <div className="card">
  <div className="card-header-row">
    <h2>Templates</h2>

    <div className="filter-tabs">
      <button
        type="button"
        className={templateFilter === "ALL" ? "filter-tab active" : "filter-tab"}
        onClick={() => setTemplateFilter("ALL")}
      >
        All
      </button>

      <button
        type="button"
        className={
          templateFilter === "ACTIVE" ? "filter-tab active" : "filter-tab"
        }
        onClick={() => setTemplateFilter("ACTIVE")}
      >
        Active
      </button>

      <button
        type="button"
        className={
          templateFilter === "ARCHIVED" ? "filter-tab active" : "filter-tab"
        }
        onClick={() => setTemplateFilter("ARCHIVED")}
      >
        Archived
      </button>
    </div>
  </div>

  {filteredTemplates.length === 0 && <p>No templates found.</p>}
            <div className="admin-list">
              {filteredTemplates.map((template) => (
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
                    <span className="template-list-title">
                      {template.title}

                      {template.archived && (
                        <small className="status-badge archived">
                          Archived
                        </small>
                      )}
                    </span>
                  </button>

                  <button
                    type="button"
                    className="secondary-small-button"
                    disabled={actionLoading}
                    onClick={() => handleEditTemplate(template)}
                  >
                    Edit
                  </button>

                  {template.archived ? (
                    <button
                      type="button"
                      className="secondary-small-button"
                      disabled={actionLoading}
                      onClick={() => handleRestoreTemplate(template.id)}
                    >
                      Restore
                    </button>
                  ) : (
                    <button
                      type="button"
                      className="secondary-small-button"
                      disabled={actionLoading}
                      onClick={() => handleArchiveTemplate(template.id)}
                    >
                      Archive
                    </button>
                  )}

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
                <div className="selected-template-header">
                  <h3>{selectedTemplate.title}</h3>

                  {selectedTemplate.archived && (
                    <small className="status-badge archived">Archived</small>
                  )}
                </div>

                {selectedTemplate.coverImageUrl ? (
                  <img
                    src={selectedTemplate.coverImageUrl}
                    alt={selectedTemplate.title}
                    className="selected-template-cover"
                  />
                ) : (
                  <div className="selected-template-cover-placeholder">
                    {selectedTemplate.title.charAt(0).toUpperCase()}
                  </div>
                )}

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
                                Offset: {task.daysOffset}, duration: {" "}
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

      <EditModal
        isOpen={isEditModalOpen}
        title={getEditModalTitle()}
        submitText={getEditSubmitText()}
        cancelText="Cancel"
        actionLoading={actionLoading}
        onSubmit={handleEditSubmit}
        onCancel={closeEditModal}
      >
        <label>
          Title
          <input
            type="text"
            value={editTitle}
            onChange={(event) => setEditTitle(event.target.value)}
            placeholder="Title"
            required
          />
        </label>

        {(editMode === "template" || editMode === "task") && (
          <label>
            Description
            <input
              type="text"
              value={editDescription}
              onChange={(event) => setEditDescription(event.target.value)}
              placeholder="Description"
            />
          </label>
        )}

        {editMode === "template" && (
          <label>
            Cover image URL
            <input
              type="text"
              value={editCoverImageUrl}
              onChange={(event) => setEditCoverImageUrl(event.target.value)}
              placeholder="https://example.com/image.jpg"
            />
          </label>
        )}

        {(editMode === "stage" || editMode === "task") && (
          <label>
            Order number
            <input
              type="number"
              value={editOrderNumber}
              onChange={(event) => setEditOrderNumber(Number(event.target.value))}
              min={1}
              required
            />
          </label>
        )}

        {editMode === "task" && (
          <>
            <label>
              Days offset
              <input
                type="number"
                value={editDaysOffset}
                onChange={(event) => setEditDaysOffset(Number(event.target.value))}
                min={0}
                required
              />
            </label>

            <label>
              Duration days
              <input
                type="number"
                value={editDurationDays}
                onChange={(event) => setEditDurationDays(Number(event.target.value))}
                min={1}
                required
              />
            </label>
          </>
        )}
      </EditModal>

      <ConfirmModal
        isOpen={confirmModalOpen}
        title={confirmTitle}
        message={confirmMessage}
        confirmText={confirmText}
        cancelText="Cancel"
        danger={confirmDanger}
        onConfirm={handleConfirmAction}
        onCancel={closeConfirmModal}
      />
    </section>
  );
}
